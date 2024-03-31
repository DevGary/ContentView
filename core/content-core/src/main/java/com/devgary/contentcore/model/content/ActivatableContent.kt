package com.devgary.contentcore.model.content

class ActivatableContent(val contentWhenNotActivated: Content, val contentWhenActivated: Content) :
    Content(contentWhenNotActivated.source, contentWhenNotActivated.type)
{
    override fun toLogString() =
        "Unactivated: ${contentWhenNotActivated.type} ${contentWhenNotActivated.source.toLogString()}, " +
                "Activated: ${contentWhenActivated.type} ${contentWhenActivated.source.toLogString()}"
}