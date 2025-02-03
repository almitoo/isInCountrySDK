# Location SDK Documentation

This SDK allows you to check if a user's location is within the borders of a specified country.
The SDK is useful for applications that require geographic location identification and allows checking if a user's location is within the borders of a specific country. It is ideal for geo-fencing apps, location-based content delivery, and restricting access to services based on country. This SDK can be used in travel apps displaying localized information by country, and in e-commerce apps that restrict products and services based on the user's location.

## How to Use

Add JitPack to your project-level `build.gradle` or `settings.gradle`:
    ```kotlin
    repositories {
        maven { url = uri("https://jitpack.io" )}
    }
    ```

    dependencies {
    implementation ("com.github.almito:isInCountrySDK:1.0.0")
}
## Available Functions

In the project directory, you can use the following function:

### `LocationSdk(Context context)`

Initialize the SDK with the context of your application.

#### Example:

```java
LocationSdk locationSdk = new LocationSdk(context);
```

### `isInCountry(String countryCode, LocationResultCallback callback)`

Check if the user's current location is within the specified country. Pass in the **country code** (e.g., "US") and provide a **callback** to handle the response.

#### Example:

```java
locationSdk.isInCountry("US", new LocationSdk.LocationResultCallback() {
    @Override
    public void onSuccess(boolean success) {
        if (success) {
            // The user is within the specified country
        } else {
            // The user is outside the specified country
        }
    }

    @Override
    public void onError(String errorMessage) {
        // Handle errors (e.g., location permission not granted, API failure, etc.)
    }
});
```





## How It Works

1. **Fused Location Provider**: The SDK uses Google's **FusedLocationProviderClient** to get the current location (latitude and longitude) of the device.
2. **API Request**: The SDK sends the location data (longitude and latitude) along with the **country code** to the backend server.
3. **Server Validation**: The server checks whether the location is inside the borders of the country specified by the country code.
4. **Response**: The SDK will return either `true` or `false` depending on whether the location is inside the specified country.

## Example API Request:

```json
{
  "longitude": 34.0522,
  "latitude": -118.2437,
  "countryCode": "US"
}
```

## Example API Response:

```json
{
  "success": true
}
```

## Error Handling

The SDK provides error messages via the `onError` callback. Common error messages include:

- **Location permissions not granted**: The app does not have permission to access location.
- **Unable to fetch location**: The SDK failed to retrieve the user's location.
- **API call failed**: The request to the backend API failed.

## Customization

- **Location Accuracy**: The SDK uses a configurable location request. You can adjust the frequency and accuracy of location updates using the `LocationRequest` object.
- **API Base URL**: production API URL (`https://vercel.com/almitos-projects/api-location`).

---
## Example of an application that used the library
![image alt](https://github.com/almitoo/isInCountrySDK/blob/08ad1a2906af5b0f4e9e258167e80570e292aac8/Screenshot%202025-02-03%20154726.png)  
![image alt](https://github.com/almitoo/isInCountrySDK/blob/bdb4813d557fd70dbccedfc275d05aaa6e00a0a6/Screenshot%202025-02-03%20154940.png)


## Contributing

If you'd like to contribute to this SDK, feel free to fork the repository and submit pull requests. Any issues or feature requests can be raised on the GitHub issue tracker.

---
