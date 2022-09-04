package com.devgary.contentview.ui.collection

import android.content.Context
import android.view.View
import com.devgary.contentcore.model.content.CollectionContent
import com.devgary.contentcore.model.content.Content
import com.devgary.contentview.ContentHandler

class CollectionContentHandler(private val context: Context) : ContentHandler {
    private var collectionContentView: CollectionContentView? = null

    override fun getView(): CollectionContentView {
        return collectionContentView ?: CollectionContentView(context = context).also { 
            collectionContentView = it
        }
    }
    
    override fun setViewVisibility(visibility: Int) {
        collectionContentView?.setViewVisibility(visibility)
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
}