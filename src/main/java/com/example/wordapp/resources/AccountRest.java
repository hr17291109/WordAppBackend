package com.example.wordapp.resources;

import com.example.wordapp.entities.Account;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Set;


@Path("/accounts")
@Component
public class AccountRest {
    // key に accountId, value に Account の HashMap を作る
    private final HashMap<String, Account> accounts = new HashMap<String, Account>();
    //private final HashMap<String, String> words = new HashMap<String, String>();

    // アカウントの一覧をリストとして返す(GET)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> getAccounts() {
        return accounts.keySet();  // この書き方で、アカウントの一覧をリストとして返せる
    }

    // account_idとpasswordを設定し新しいアカウントを作成する(POST)
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)  // bodyに入力する値がある時
    public void signup(@FormParam("account_id") String accountId, @FormParam("password") String password) {
        // パスワードが入力されていない(null)の場合
        if (password == null) {
            var response = Response.status(Response.Status.BAD_REQUEST) // 404
                    .entity("passwordを入力してください"); // postmanにこう表示される
            throw new WebApplicationException(response.build());
        } else {
            // アカウントが既に存在した場合
            if (accounts.containsKey(accountId)) {
                var response = Response.status(Response.Status.CONFLICT)  // 409
                        .entity("id '" + accountId + "' は既に存在します");  // postmanにこう表示される
                throw new WebApplicationException(response.build());
            }
            // アカウント作成の処理 200
            Account account = new Account(accountId, password);
            accounts.put(accountId, account);
        }
    }

    // 指定されたアカウントの単語帳を返す(GET)
    @Path("/{account_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Account getAccountInfo(@PathParam("account_id") String accountId, @QueryParam("password") String password) {
        // account_idが存在しない場合 404
        if (!accounts.containsKey(accountId)) {
            var response = Response.status(Response.Status.NOT_FOUND).entity("IDが存在しません");
            throw new WebApplicationException(response.build());
        }
        Account account = accounts.get(accountId);
        // passwordが存在しない場合 401
        if (!account.getPassword().equals(password)) {
            var response = Response.status(Response.Status.UNAUTHORIZED).entity("パスワードが間違っています");
            throw new WebApplicationException(response.build());
        }
        // 単語帳を返す 200
        return account;
    }

    // passwordを変更する(PUT)
    @Path("/{account_id}/password")
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)  // bodyに入力する値がある時
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public void changePW(@FormParam("new_password") String newPassword,
                         @FormParam("old_password") String oldPassword,
                         @PathParam("account_id") String accountId) {
        // パスワードが間違っている場合 401
        if (!accounts.get(accountId).getPassword().equals(oldPassword)) {
            var response = Response.status(Response.Status.UNAUTHORIZED)
                    .entity("パスワードが違います");
            throw new WebApplicationException(response.build());
        }
        // パスワードを置き換える 200
        accounts.get(accountId).setPassword(newPassword);
    }

    @Path("/{account_id}/words")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addWord(@FormParam("new_English_word") String newEnWord,
                         @FormParam("new_Japanese_meaning") String JpMeaning,
                         @PathParam("account_id") String accountId,
                         @FormParam("password") String password) {
        if (!accounts.get(accountId).getPassword().equals(password)) {
            var response = Response.status(Response.Status.UNAUTHORIZED)
                    .entity("パスワードが違います");
            throw new WebApplicationException(response.build());
        }
        Account account = accounts.get(accountId);
        HashMap<String, String> words = account.getWords();
        if (newEnWord == null) {
            var response = Response.status(Response.Status.BAD_REQUEST) // 404
                    .entity("英単語を入力してください"); // postmanにこう表示される
            throw new WebApplicationException(response.build());
        } else if (JpMeaning == null) {
            var response = Response.status(Response.Status.BAD_REQUEST) // 404
                    .entity("日本語訳を入力してください"); // postmanにこう表示される
            throw new WebApplicationException(response.build());
        } else {
            // 英単語が既に存在した場合
            if (words.containsKey(newEnWord)) {
                var response = Response.status(Response.Status.CONFLICT)  // 409
                        .entity("id '" + accountId + "' は既に存在します");  // postmanにこう表示される
                throw new WebApplicationException(response.build());
            }
            words.put(newEnWord, JpMeaning);
        }
    }
}
