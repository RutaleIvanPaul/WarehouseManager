package io.ramani.ramaniWarehouse.app.common.di

import io.ramani.ramaniWarehouse.BuildConfig
import io.ramani.ramaniWarehouse.data.common.network.*
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.*
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Created by Amr on 9/22/17.
 */
val networkModule = Kodein.Module("networkModule") {
    bind<NetworkManager>() with singleton {
        NetworkManager(instance())
    }
    bind<PrefsManager>() with singleton {
        PrefsManager(instance())
    }

    bind<Retrofit>() with singleton {
        Retrofit.Builder()
            .baseUrl(instance<String>("baseUrl"))
            .client(instance())
            .addCallAdapterFactory(instance())
            .addConverterFactory(instance("scalarConverterFactory"))
            .addConverterFactory(instance())
            .build()
    }

    bind<CallAdapter.Factory>() with provider {
        RxErrorHandlingCallAdapterFactory.create(instance())
    }

    bind<Converter.Factory>() with provider {
        GsonConverterFactory.create()
    }

    bind<Converter.Factory>(tag = "scalarConverterFactory") with provider {
        ScalarsConverterFactory.create()
    }

    bind<OkHttpClient>() with provider {
        OkHttpClientHelper.buildClient(
            instance(),
            instance("connectionTimeOut"),
            instance("readTimeOut"),
            instance("writeTimeOut"),
            instance("headersInterceptor"),
            instance(),
            instance()
        )
    }

    bind<HeadersProvider>() with provider {
        GenericHeadersProvider(instance())
    }

//    bind<ChuckInterceptor>() with provider {
//        ChuckInterceptor(instance())
//    }

    bind<NetworkErrorHandler>() with provider {
        GenericNetworkErrorHandler(instance())
    }

    constant("baseUrl") with BuildConfig.BASE_URL
    constant("connectionTimeOut") with NetworkConstants.CONNECT_TIME_OUT
    constant("readTimeOut") with NetworkConstants.READ_TIME_OUT
    constant("writeTimeOut") with NetworkConstants.WRITE_TIME_OUT

    bind<Interceptor>() with provider {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    bind<Interceptor>("headersInterceptor") with provider {
        HeadersInterceptor(instance())
    }
}