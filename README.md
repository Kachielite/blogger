# Blogger App

Welcome to the Blogger App! This project is currently in development and aims to provide a comprehensive platform for bloggers to create, collaborate, and share their content with the world.

## Features

- **Roles-Based Authentication**: Utilizes JSON Web Tokens (JWT) for secure authentication, with two user roles: ADMIN and USER. ADMIN users have additional privileges, including managing users and their roles.

- **Blog Sessions**: Allows users to create, publish, and collaborate on blog posts within the platform. Future plans include enhancing the writing experience with AI integration.

- **User Profile Customization**: Users can personalize their profiles with avatars, bios, and other customization options.

- **Social Media Integration**: Enables users to share their blog posts directly to popular social media platforms.

- **Real-Time Collaboration**: Incorporates real-time editing and commenting features for a dynamic blogging experience.

- **Analytics Dashboard**: Provides users with insights into their blog's performance, including page views, audience demographics, and popular topics.

- **Content Recommendation Engine**: Utilizes AI to recommend relevant blog posts based on user preferences and behavior.

## Data Structure

- **User and Role Management**: Utilizes a joined table for user and role management.

- **Separate Tables for Posts and Comments**: Organizes blog content into separate tables for better scalability and organization.

- **Tagging System**: Implements a tagging system for categorizing blog posts and improving discoverability.

- **Versioning for Posts**: Supports versioning functionality to track changes and revert to previous versions if necessary.

- **Media Storage**: Integrates with a media storage solution ([Cloudinary](https://cloudinary.com/)) for efficient storage and serving of multimedia content.

## Development

This project is currently in development. Contributions, suggestions, and feedback are welcome! To get started, follow the instructions below:

1. Clone the repository:

    ```bash
    git clone https://github.com/Kachielite/blogger.git
    ```

2. Navigate to the project directory:

    ```bash
    cd blogger-app
    ```

3. Install dependencies:

    ```bash
    ./gradlew build
    ```


4. Run the application:

    ```bash
    java -jar build/libs/blogger-app.jar
    ```

5. Access the API Docs on

     ```
    http://localhost:8080/api/v1/swagger-ui/index.html
    ```

## Database

The app uses PostgreSQL as its database backend, hosted on [Render](https://render.com/).

## Technologies

- **Spring Boot**: Provides a robust framework for building Java applications.

- **Gradle**: Used for project build automation and dependency management.

## Contributing

Contributions are encouraged! If you have ideas for new features, improvements, or bug fixes, please open an issue or submit a pull request.

[//]: # (## License)

[//]: # ()
[//]: # (This project is licensed under the [MIT License]&#40;LICENSE&#41;.)
