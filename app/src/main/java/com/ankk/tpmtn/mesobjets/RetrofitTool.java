package com.ankk.tpmtn.mesobjets;

import java.security.Provider;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RetrofitTool {

    public OkHttpClient getClient(boolean interceptor, String userToken) {
        Provider conscrypt = null;
        OkHttpClient okHttpClient = null;
        try {
            /*if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                conscrypt = Conscrypt.newProvider();
                // Add as provider
                Security.insertProviderAt(conscrypt, 1);
            }*/

            // Init OkHttp
            OkHttpClient.Builder okHttpBuilder = new OkHttpClient()
                    .newBuilder()
                    //.connectionSpecs(Collections.singletonList(ConnectionSpec.RESTRICTED_TLS))
                    .readTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    ;
            if(interceptor){
                okHttpBuilder.addInterceptor(chain -> {
                    Request request = chain
                            .request()
                            .newBuilder()
                            //.addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", ("Bearer " + userToken))
                            .addHeader("isRefreshToken", "true")
                            .build();
                    okhttp3.Response poc = chain.proceed(request);
                    return poc;
                });
            }

            /*if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                X509TrustManager tm = Conscrypt.getDefaultX509TrustManager();
                SSLContext sslContext = SSLContext.getInstance("TLS", conscrypt);
                sslContext.init(null, new TrustManager[]{tm}, null);
                okHttpBuilder.sslSocketFactory(new TLSSocketFactory(sslContext.getSocketFactory()), tm);
            }*/

            okHttpClient = okHttpBuilder.build();
        }
        catch (Exception exc){
        }

        return okHttpClient;
    }

}
