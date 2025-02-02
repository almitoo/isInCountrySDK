plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish) // Apply the maven-publish plugin
}

android {
    namespace = "com.example.isincountry"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Play Services for location
    implementation("com.google.android.gms:play-services-location:21.0.1")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.github.almitoo"
                artifactId = "isInCountry"
                version = "1.0.0"
                artifact(tasks.getByName("bundleReleaseAar"))

                // Add dependencies to the Maven publication configuration (api or implementation)

            }
        }
    }
}

