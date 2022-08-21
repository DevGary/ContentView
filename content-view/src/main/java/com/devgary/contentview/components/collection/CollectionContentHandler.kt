package com.devgary.contentview.components.collection

import android.content.Context
import android.view.View
import com.devgary.contentcore.model.CollectionContent
import com.devgary.contentcore.model.Content
import com.devgary.contentview.content.ContentHandler

class CollectionContentHandler(private val context: Context) : ContentHandler {
    private var collectionContentView: CollectionContentView? = null
  
    private fun createView() {
        if (collectionContentView != null) return
        collectionContentView = CollectionContentView(context = context)
    }

    override fun getView(): View {
        createView()
        return collectionContentView!!
    }
    
    override fun setViewVisibility(visibility: Int) {
        collectionContentView?.setViewVisibility(visibility)
    }
    
    override fun canShowContent(content: Content): Boolean {
        return content is CollectionContent
    }
    
    override fun showContent(content: Content) {
        createView()
        setViewVisibility(View.VISIBLE)

        (content as CollectionContent).let {
            collectionContentView?.showContent(content)
        }
    }
}