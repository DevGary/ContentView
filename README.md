# ContentView

ContentView is a library to simplify loading content such as Images, Gifs, Videos, etc. It consists of 2 modular components that can be used together or independently:

- `ContentLinkHandler` converts urls to sites like Imgur, Gfycat, Streamable, etc into `Content` by calling the respective APIs and mapping the responses into `Content` items
- `ContentView` is the Android `View` that supports loading different types of `Content` with the help of libraries like `Glide`, `ExoPlayer`, `ViewPager2`, etc.

![](demo.webp)

# Status
The library is still in **early** in development and I would not recommend using it in production, specifically the `ContentView` component.
