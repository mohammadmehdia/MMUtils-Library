package ir.alizadeh.mmui.components.base;

public interface IRetroComponent {
    void retrofitLoading(boolean show);
    void retrofitError(String message, String tag);
}
