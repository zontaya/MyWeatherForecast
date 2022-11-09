val kotlinVersion: String by project
val koinVersion: String by project
val okhttpVersion: String by project
val naviVersion: String by project
val retrofitVersion: String by project
val androidCoreVersion: String by project
val appcompatVersion: String by project
val materialVersion: String by project
val gsonVersion: String by project
val kotlinDatetimeVersion: String by project
val constraintLayoutVersion: String by project
val picassoVersion: String by project
val dexterVersion: String by project
val lottieVersion: String by project
val mockitoVersion: String by project

plugins {
    kotlin("android")
    kotlin("kapt")
    id("com.android.application")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {

    compileSdk = 33

    defaultConfig {
        applicationId = "app.sonlabs.myweatherforecast"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

}

dependencies {
    implementation("androidx.core:core-ktx:$androidCoreVersion")
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$naviVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$naviVersion")

    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation("com.google.android.material:material:$materialVersion")

    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:$okhttpVersion")
    implementation("com.squareup.picasso:picasso:$picassoVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinDatetimeVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-androidx-compose:$koinVersion")
    implementation("io.insert-koin:koin-test:$koinVersion")

    testImplementation("io.insert-koin:koin-test:$koinVersion")
    testImplementation("io.insert-koin:koin-test-junit4:$koinVersion")

    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.test.ext:junit-ktx:1.1.3")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:3.11.2")
    testImplementation("app.cash.turbine:turbine:0.12.1")
    androidTestImplementation("org.mockito:mockito-android:3.10.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation("com.airbnb.android:lottie:$lottieVersion")
    implementation("com.karumi:dexter:$dexterVersion")
}
