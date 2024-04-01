# ContentView

# Status
The library is still in **early** development and I would not recommend using it in production, especially the `ContentView` component.

ContentView is a library to simplify loading content such as Images, Gifs, Videos, Albums, from websites such as [Imgur](https://imgur.com/), [Gfycat](https://gfycat.com/), and [Streamable](https://streamable.com/). It consists of 2 modular components that can be used together or independently.

- `ContentLinkHandler` converts urls to sites such as Imgur, Gfycat, Streamable, etc into `Content` by calling the respective APIs and mapping the responses into `Content` items.
- `ContentView` is an Android `View` that supports loading different types of `Content` with the help of libraries such as `Glide`, `ExoPlayer`, `ViewPager2`, etc.

![](demo.webp)

# Code Structure
Both `ContentLinkHandler` and `ContentView` follow a similar structure. 

Content Link Handlers must implement the `ContentLinkHandler` interface that contains a function that returns whether the `ContentLinkHandler` can handle a certain `url` and another function that converts the `url` into a `Content` item, usually by making some sort of HTTP Request. Then, all the individual `ContentLinkHandlers` (eg `StreamableContentLinkHandler`, `ImgurContentLinkHandler`) are combined using `AbstractCompositeContentLinkHandler` which itself implements the same `ContentLinkHandler` interface but handles content links using the individual `ContentLinkHandlers`.

Similarily, Content Views must implement the `ContentHandler` interface that contains a function that returns whether the `ContentHandler` can show some `Content` and  another function that shows the `Content` on a View. Then, all the individual `ContentHandlers` (eg `ImageContentHandler`, `VideoContentHandler`) are combined using `AbstractCompositeContentHandlerView` which itself implements the same `ContentHandler` interface but handles content using the individual `ContentHandlers`.

# Instructions
```gradle
repositories {
  maven { url 'https://jitpack.io' }
}
```

```gradle
implementation "com.github.DevGary.ContentView:content-link-api:<version>"
implementation "com.github.DevGary.ContentView:content-view:<version>"
```

See [Releases](https://github.com/DevGary/ContentView/releases) for versions
