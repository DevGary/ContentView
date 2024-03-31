package com.devgary.contentview

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.contains
import com.devgary.contentcore.model.content.ActivatableContent
import com.devgary.contentcore.model.content.Content
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.classNameWithValue
import com.devgary.contentcore.util.name
import com.devgary.contentcore.util.removeFromParentView
import com.devgary.contentview.interfaces.Activatable
import com.devgary.contentview.interfaces.Disposable
import com.devgary.contentview.interfaces.PlayPausable
import com.devgary.contentview.interfaces.Recyclable
import com.devgary.contentview.model.ScaleType
import com.devgary.contentview.util.generateIdIfNotExists
import com.devgary.contentview.util.setGravityFrameLayoutParent

abstract class AbstractCompositeContentHandlerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), ContentHandler, Disposable, PlayPausable, Activatable {

    private val contentHandlers = mutableListOf<ContentHandler>()
    var currentlyUsedHandler: ContentHandler? = null
        private set
    var content: Content? = null
        private set
    private var viewPoolComposite: ViewPoolComposite? = null

    init {
        registerContentHandlers()
        setViewScaleType(ScaleType.FIT_CENTER)
    }

    private fun registerContentHandlers() {
        for (providedContentHandler in createContentHandlers()) {
            contentHandlers.add(providedContentHandler)
        }
    }
    
    abstract fun createContentHandlers(): List<ContentHandler>

    override fun getView() = this

    override fun setViewVisibility(visibility: Int) {
        setContentHandlersVisibility(visibility)
    }
    
    private fun setContentHandlersVisibility(visibility: Int, excludedContentHandler: ContentHandler? = null) {
        Log.d(TAG, "Hiding every ${name<ContentHandler>()} except $excludedContentHandler")
        contentHandlers
            .filter { handler -> handler != excludedContentHandler }
            .forEach { handler -> handler.setViewVisibility(visibility) }
    }

    final override fun setViewScaleType(scaleType: ScaleType) {
        contentHandlers
            .forEach { handler -> handler.setViewScaleType(scaleType) }
    }

    fun attachViewPool(viewPoolComposite: ViewPoolComposite) {
        this.viewPoolComposite = viewPoolComposite

        contentHandlers
            .filter { contentHandler -> contentHandler is Recyclable }
            .forEach { contentHandler ->
                (contentHandler as Recyclable).let {
                    val viewPool = viewPoolComposite.getOrCreatePool(
                        clazz = contentHandler.javaClass.kotlin,
                        viewPoolCreator = { it.getOrCreateViewPool() }
                    )

                    it.setViewPool(viewPool)
                }
            }
    }
    
    override fun showContent(content: Content) {
        this.content = content
        showContentInternal(content)
    }
    
    private fun showContentInternal(content: Content) {
        Log.d(TAG, "showContentInternal(content = $content) called")
        val firstContentHandlerForContent = contentHandlers.firstOrNull { handler ->
            handler.canShowContent(content)
        }
        
        firstContentHandlerForContent?.let { handler ->
            currentlyUsedHandler = handler
            addContentHandlerViewIfNotAdded(handler)
            handler.showContent(content)
            setContentHandlersVisibility(GONE, excludedContentHandler = currentlyUsedHandler)
        } ?: run {
            Log.e(TAG, "No ${name<ContentHandler>()} found for ${classNameWithValue(content)}")
        }
    }

    override fun canShowContent(content: Content): Boolean {
        return contentHandlers.any { handler -> handler.canShowContent(content) }
    }

    private fun addContentHandlerViewIfNotAdded(handler: ContentHandler) {
        val contentHandlerView = handler.getView()
        addViewIfNotExist(contentHandlerView)
    }
    
    private fun addViewIfNotExist(viewToAdd: View) {
        if (contains(viewToAdd)) return
        
        processViewBeforeAdding(viewToAdd)
        addView(viewToAdd)
        processViewAfterAdding(viewToAdd)
    }
    
    private fun processViewBeforeAdding(view: View) {
        view.removeFromParentView()
        view.generateIdIfNotExists()
    }
    
    private fun processViewAfterAdding(view: View) {
        view.setGravityFrameLayoutParent(Gravity.CENTER)
    }

    override fun dispose() {
        contentHandlers.forEach {
            (it as? Disposable)?.dispose()
        }
        recycle()
    }
    
    private fun recycle() {
        contentHandlers.forEach {
            (it as? Recyclable)?.recycle()
        }
    }
    
    override fun play() {
        contentHandlers.forEach {
            (it as? PlayPausable)?.play()
        }
    }    
    
    override fun pause() {
        contentHandlers.forEach {
            (it as? PlayPausable)?.pause()
        }
    }

    override fun setAutoplay(autoplay: Boolean) {
        contentHandlers.forEach {
            (it as? PlayPausable)?.setAutoplay(autoplay)
        }
    }

    override fun activate() {
        (content as? ActivatableContent)?.let {
            showContentInternal(it.contentWhenActivated)
        }
    }

    override fun deactivate() {
        (content as? ActivatableContent)?.let {
            showContentInternal(it.contentWhenNotActivated)
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val state = super.onSaveInstanceState()
        
        state?.let {
            val savedState = SavedState(it)
            savedState.positionOfLastUsedContentHandler = contentHandlers.indexOf(currentlyUsedHandler)
            return savedState
        } ?: run {
            return null
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            contentHandlers.getOrNull(state.positionOfLastUsedContentHandler)?.let {
                // In onRestoreInstanceState(), immediately add back the view that was
                // being used to display content. Otherwise, since the ContentHandlerViews
                // are lazily created, there is a risk that when onRestoreInstanceState()
                // is propagated throughout the views, it is missed by the ContentHandlerView
                // since it has not been created back yet and its state is not restored
                addContentHandlerViewIfNotAdded(it)
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }
    
    internal class SavedState : BaseSavedState {
        // TODO: Use better way of tracking which ContentHandler used than position
        var positionOfLastUsedContentHandler = -1

        constructor(source: Parcel) : super(source) {
            positionOfLastUsedContentHandler = source.readInt()
        }

        constructor(superState: Parcelable) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(positionOfLastUsedContentHandler)
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}