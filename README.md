# ContentView

ContentView is a library to simplify loading content such as Images, Gifs, Videos, Albums, from websites such as [Imgur](https://imgur.com/), [Gfycat](https://gfycat.com/), and [Streamable](https://streamable.com/). It consists of 2 modular components that can be used together or independently.

- `ContentLinkHandler` converts urls to sites like Imgur, Gfycat, Streamable, etc into `Content` by calling the respective APIs and mapping the responses into `Content` items.
- `ContentView` is the Android `View` that supports loading different types of `Content` with the help of libraries like `Glide`, `ExoPlayer`, `ViewPager2`, etc.

![](demo.webp)

# Instructions
```gradle
implementation "com.github.DevGary.ContentView:content-link-api:<version>"
implementation "com.github.DevGary.ContentView:content-view:<version>"
```

See [Releases](https://github.com/DevGary/ContentView/releases) for versions

# Status
The library is still in **early** in development and I would not recommend using it in production, especially the `ContentView` component.
