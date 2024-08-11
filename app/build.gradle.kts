import com.android.build.api.dsl.Packaging

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.foodapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.foodapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    packaging {
        resources.excludes.add("META-INF/atomicfu.kotlin_module")
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth-ktx:23.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.3")

    implementation ("com.google.code.gson:gson:2.8.8")
//
//
//    implementation ("androidx.activity:activity-ktx:$rootProject.activityVersion")
//
//    // Dependencies for working with Architecture components
//    // You'll probably have to update the version numbers in build.gradle (Project)
//
//    // Room components
//    implementation ("androidx.room:room-ktx:$rootProject.roomVersion")
//    kapt ("androidx.room:room-compiler:$rootProject.roomVersion")
//    androidTestImplementation ("androidx.room:room-testing:$rootProject.roomVersion")
//
//    // Lifecycle components
//    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$rootProject.lifecycleVersion")
//    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$rootProject.lifecycleVersion")
//    implementation ("androidx.lifecycle:lifecycle-common-java8:$rootProject.lifecycleVersion")
//
//    // Kotlin components
//    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:kotlin_version")
//    api ("org.jetbrains.kotlinx:kotlinx-coroutines-core:$rootProject.coroutines")
//    api ("org.jetbrains.kotlinx:kotlinx-coroutines-android:$rootProject.coroutines")
//
//
//    // Testing
//    testImplementation ("junit:junit:$rootProject.junitVersion")
//    androidTestImplementation ("androidx.arch.core:core-testing:$rootProject.coreTestingVersion")
//    androidTestImplementation ("androidx.test.espresso:espresso-core:$rootProject.espressoVersion", {
//        exclude (group= "com.android.support", module= "support-annotations")
//    })
//    androidTestImplementation ("androidx.test.ext:junit:$rootProject.androidxJunitVersion")
}
