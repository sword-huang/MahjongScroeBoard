import java.util.Calendar

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.androidx.navigation.safeargs.kotlin)
}

val version_name = String.format("1.0.%tY%<tm%<td%<tH%<tM", Calendar.getInstance())
val calendar = Calendar.getInstance()
val release_date = "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}"

android {
    namespace = "com.example.mahjongscroeboard"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mahjongscroeboard"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = version_name
        buildConfigField("String", "VERSION_NAME", "\"${version_name}\"")
        buildConfigField("String", "RELEASE_DATE", "\"${release_date}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            setProperty("archivesBaseName", "MahjongScoreBoard-$version_name")
        }
        debug {
            setProperty("archivesBaseName", "MahjongScoreBoard-$version_name")
        }
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
        buildConfig = true
    }

    
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

    