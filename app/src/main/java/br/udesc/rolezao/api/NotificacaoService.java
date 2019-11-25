package br.udesc.rolezao.api;

import br.udesc.rolezao.model.NotificacaoDados;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificacaoService {
    @Headers({
            "Authorization:key=AAAAPCrvWQ0:APA91bHO4MiWCDKtECQ7Uxwbmh14f0eGTkEpJWDavnhfwjM2xgreqeK7i9L0Y_Yi-oPjWpvMSFKYPsZw2pAqkHajTk55JUFetnyzfqo3x3nXGM_TevxgGm62JhdhNASf2J6GdhUixf7H",
            "Content-Type:application/json"
    })
    @POST("send")
    Call<NotificacaoDados> salvarNotificacao(@Body NotificacaoDados notificacaoDados);
}
