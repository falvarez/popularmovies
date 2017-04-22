# Popular Movies

Udacity training project stage 2.

## Features

* You’ll allow users to view and play trailers ( either in the youtube app or a web browser).
* You’ll allow users to read reviews of a selected movie.
* You’ll also allow users to mark a movie as a favorite in the details view by tapping a button(star). This is for a local movies collection that you will maintain and does not require an API request.
* You’ll modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.

## Technical specifications

### User Interface - Layout

* UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.
* Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
* UI contains a screen for displaying the details for a selected movie.
* Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.
* Movie Details layout contains a section for displaying trailer videos and user reviews.

### User Interface - Function

* When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.
* When a movie poster thumbnail is selected, the movie details screen is launched.
* When a trailer is selected, app uses an Intent to launch the trailer.
* In the movies detail screen, a user can tap a button(for example, a star) to mark it as a Favorite.

### Network API Implementation

* In a background thread, app queries the `/movie/popular` or `/movie/top_rated` API for the sort criteria specified in the settings menu.
* App requests for related videos for a selected movie via the `/movie/{id}/videos` endpoint in a background thread and displays those details when the user selects a movie.
* App requests for user reviews for a selected movie via the `/movie/{id}/reviews` endpoint in a background thread and displays those details when the user selects a movie.

### Data persistence

* The titles and ids of the user's favorite movies are stored in a `ContentProvider` backed by a SQLite database. This `ContentProvider` is updated whenever the user favorites or unfavorites a movie.
* When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the `ContentProvider`.

### General project guidelines

## Build instructions

You will need an API key from https://www.themoviedb.org/ and store it in local gradle properties file. The variable name is `MovieDbApiKey`.

## API calls

Most popular: https://api.themoviedb.org/3/movie/popular?api_key=API_KEY

Top rated: https://api.themoviedb.org/3/movie/top_rated?api_key=API_KEY

Trailers: https://api.themoviedb.org/3/movie/{id}/videos?api_key=API_KEY

Reviews: https://api.themoviedb.org/3/movie/{id}/reviews?api_key=API_KEY


## Reference projects

* https://github.com/udacity/ud851-Sunshine/
* https://github.com/udacity/ud851-Exercises/
