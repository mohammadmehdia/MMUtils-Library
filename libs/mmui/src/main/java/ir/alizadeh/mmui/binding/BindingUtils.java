package ir.alizadeh.mmui.binding;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.List;

import ir.alizadeh.mmui.glide.glide.GlideNullTransition;
import ir.alizadeh.mmui.iv.AspectRatioImageView;
import ir.alizadeh.mmui.utils.ProgressBarAnimation;
import ir.alizadeh.mmui.utils.Utils;

@SuppressLint("UnknownNullness")

public class BindingUtils {
    private static final String PREFNAME = "binding_utils_pref";
    private static final String TAG = BindingUtils.class.getSimpleName();
    private static String mediaPrefixUrl;

    public static void setMediaPrefixUrl(String url) {
        mediaPrefixUrl = url;
    }

    public static String getMediaPrefixUrl() {
        return mediaPrefixUrl;
    }

    public static String mediaUrl(String url) {
        String prefix = getMediaPrefixUrl();
        if(TextUtils.isEmpty(prefix) || TextUtils.isEmpty(url) || url.startsWith("http")) {
            return url;
        } else {
            return prefix + url;
        }
    }

    @BindingAdapter("srcDrawableId")
    public static void bindSrcDrawable(AppCompatImageView imageView, int drawableId) {
        try {
            if(drawableId == 0) {
                imageView.setImageResource(drawableId);
            } else {
                Glide.with(imageView.getContext())
                        .load(drawableId)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .apply(RequestOptions.skipMemoryCacheOf(false))
                        .into(imageView);
            }
        } catch (Exception ignored) {}
    }

    @BindingAdapter("srcDrawable")
    public static void bindSrcDrawable(AppCompatImageView imageView, Drawable drawable) {
        try {
            if(drawable != null) {
                Glide.with(imageView.getContext()).load(drawable).into(imageView);
            } else {
                imageView.setImageResource(0);
            }
        } catch (Exception ignored) {}
    }


    @BindingAdapter("imageUrl")
    public static void bindImageUrl(AppCompatImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(mediaUrl(imageUrl))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .transition(DrawableTransitionOptions.withCrossFade(100))
//                .transition(DrawableTransitionOptions.with(new GlideNullTransition()))
                .into(imageView);
    }

    @BindingAdapter("imageUrlNoTransition")
    public static void bindImageUrlNoTransition(AppCompatImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(mediaUrl(imageUrl))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .transition(DrawableTransitionOptions.with(new GlideNullTransition()))
                .into(imageView);
    }

    @BindingAdapter("imageUrlCircleCrop")
    public static void bindImageUrlCircleCrop(AppCompatImageView imageView, String imageUrl) {
            Glide.with(imageView.getContext())
                    .load(mediaUrl(imageUrl))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .skipMemoryCache(false)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(imageView);
    }

    @BindingAdapter("webHtml")
    public static void bindWebContent(WebView webView, String html) {
        if(TextUtils.isEmpty(html)) {
            webView.setVisibility(View.GONE);
        } else {
            String text = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style1.css\" />" + html;
            webView.setBackgroundColor(Color.argb(1, 0,0,0));
//            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
            webView.loadDataWithBaseURL("file:///android_asset/", text, "text/html", "UTF-8", null);
            webView.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter("textHtml")
    public static void bindTextHtml(AppCompatTextView textView, String html) {
        if(TextUtils.isEmpty(html)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
            } else {
                textView.setText(Html.fromHtml(html));
            }
        }
    }

    @BindingAdapter("relativeTime")
    public static void bindRelativeTime(AppCompatTextView textView, String time) {
        String t = Utils.relativeTime(textView.getContext(), time);
        textView.setText(t);
    }

    @BindingAdapter("aspectRatioStr")
    public static void bindMyIvAspectRatio(AspectRatioImageView imageView, String aspectRatio) {
        try {
            String[] parts = aspectRatio.split("/");
            imageView.setRatio(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        } catch (Exception ignored){}
    }

    @BindingAdapter("mediaDuration")
    public static void bindMediaDurationText(AppCompatTextView textView, int duration) {
        String t = Utils.mediaDuration(duration);
        textView.setText(t);
    }

    @BindingAdapter("linearReverseOrder")
    public static void bindLinearReverseOrder(LinearLayout linearLayout, boolean reverse) {
        try {
            if(reverse) {
                List<View> views = new ArrayList<>();
                int count = linearLayout.getChildCount();
                for(int i = 0; i < count; i++) {
                    views.add(linearLayout.getChildAt(i));
                }
                linearLayout.removeAllViews();
                for(int i = count-1; i >= 0; i--) {
                    linearLayout.addView(views.get(i));
                }
            }
        } catch (Exception ignored){}
    }

    @BindingAdapter({"pushDownAnim", "pushDownAnimScale"})
    public static void bindPushDownAnimation(View view, boolean enable, float scale) {
        if(enable) {
            PushDownAnim.setPushDownAnimTo(view).setScale(PushDownAnim.MODE_SCALE, scale);
        }
    }

    @BindingAdapter("tintEnable")
    public static void bindImageTintEnable(AppCompatImageView imageView, boolean enable) {
        if(!enable) {
            imageView.setColorFilter(Color.TRANSPARENT);
        }
    }

    @BindingAdapter("progress_animated")
    public static void bindProgressBarProgressAnimated(ProgressBar progressBar, int progress) {
        progressBar.clearAnimation();
        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, progressBar.getProgress(), progress);
        anim.setDuration(700);
        progressBar.startAnimation(anim);
    }

    @BindingAdapter("bgColorInt")
    public static void bindBgColor(View view, int color) {
        view.setBackgroundColor(color);
    }

}
