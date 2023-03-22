package com.devgary.contentcore.model.content

class ActivatableContent(val contentWhenNotActivated: Content, val contentWhenActivated: Content) :
    Content(contentWhenNotActivated.source, contentWhenNotActivated.type)