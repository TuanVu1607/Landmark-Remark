
# Approach Overview
My approach to developing the Landmark Remark application was structured into several key stages, each addressing a different aspect of the project. The primary focus was to ensure a smooth and efficient user experience while leveraging modern development techniques and frameworks for location-based services.

    1. Requirements Gathering & Planning: I began by outlining the project requirements, including note management,  user authentication and map integration. I also defined the technical stack and design patterns that would best suit the app's goals.

    2. Frontend Development(Android Native): The front-end portion was developed using Kotlin Language and follow the MVVM architecture pattern, which stands for "Model-View-Model". This included designing the UI view for registration, login, and map interaction.

    3. Data management: I used the RoomDB to store and manage the data of user and address notes for the application. Using SharedPreferences to caching user authentication.

    4. Map Integration(Google Maps API): I integrated Google Maps into the application for users to interact with locations, drop pins, and view remarks left by others. This required setting up the Google Maps API, handling map events, and linking it with the RoomDB data and retrieve location-based notes.

    5. State Management & Real-time Sync: I used LiveData combine with ViewModel to manage state and sync data from Local Database.

    6. Testing & Debugging: After completing the core features, I tested the app to ensure compatibility and stability. Bug fixing and performance optimizations were performed iteratively during this phase.
## Time Breakdown

 - Planning & Setup: 2 hours
 - Frontend (UI & Logic): 10 hours
 - Database (Model, Entity, Query, SharedPreferences): 8 hours
 - Map Integration: 5 hours
 - State Management: 4 hours
 - Testing & Debugging: 6 hours
 - Total Time Spent: ~35 hours


## Known Issues or Limitations

- Limited Offline/Online Functionality: the application need an internet connection for call api service from geolocation(Google Api) to get address text of the location. do not have backend service to store the data in cloud service.
- Performance on Large Datasets: When the number of notes grows significantly, there may be a performance hit due to the lack of pagination or optimization in querying large data sets.
- No Advanced Search Filtering: The current implementation supports basic keyword search(username, note, and address), but more advanced filtering options (e.g., by location radius, date range) have not been implemented.
- Map API Limits: The free tier of the Google Maps API comes with usage limits, which might affect the app's performance if the number of users or map interactions increases significantly.
- Authentication Security: just simple caching user and register, but additional layers of security could improve the overall security of the app.