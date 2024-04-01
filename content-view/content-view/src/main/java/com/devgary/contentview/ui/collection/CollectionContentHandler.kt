package com.devgary.contentview.ui.collection

import android.content.Context
import android.view.View
import com.devgary.contentcore.model.content.CollectionContent
import com.devgary.contentcore.model.content.Content
import com.devgary.contentview.ContentHandler
import com.devgary.contentview.interfaces.Disposable
import com.devgary.contentview.model.ScaleType

class CollectionContentHandler(private val context: Context) : ContentHandler, Disposable {
    private var collectionContentView: CollectionContentView? = null
    private var scaleType: ScaleType? = null

    override fun getView(): CollectionContentView {
        return collectionContentView ?: CollectionContentView(context = context).also { 
            collectionContentView = it.also { 
                it.scaleType = scaleType
            }
        }
    }
    
    override fun setViewVisibility(visibility: Int) {
        collectionContentView?.setViewVisibility(visibility)
    }

    override fun setViewScaleType(scaleType: ScaleType) {
        this.scaleType = scaleType
        collectionContentView?.scaleType = scaleType
    }
    
    override fun canShowContent(content: Content): Boolean {
        return content is CollectionContent
    }
    
    override fun showContent(content: Content) {
        getView()
        setViewVisibility(View.VISIBLE)

        (content as CollectionContent).let {
            collectionContentView?.showContent(content)
        }
    }

    override fun dispose() {
        collectionContentView?.dispose()
    }
}