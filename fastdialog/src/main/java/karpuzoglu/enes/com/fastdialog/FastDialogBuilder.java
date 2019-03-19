package karpuzoglu.enes.com.fastdialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;

/**
 * Created by ENES on 7.12.2018.
 */
public class FastDialogBuilder {
    private Dialog dialog;
    private Context context;
    private TextView tvTitle;
    private TextView tvProgress;
    private LottieAnimationView lawWarning;
    private TextView tvWarning;
    private ClearableEditText etWarning;
    private ClearableEditText etWarningDecimal;
    private ClearableEditText etUsername;
    private ClearableEditText etPassword;
    private Button btCancel;
    private Button btOk;
    private PositiveClick positiveClick;
    private NegativeClick negativeClick;
    private DismissListener dismissListener;

    private FolderAdapter folderAdapter;
    private LinearLayout folderLayout;
    private RecyclerView rvFolder;

    private boolean isDecimal = false;
    private boolean fullScreen = true;
    private Type type;


    public FastDialogBuilder(@NonNull Context context,@NonNull Type dialogType){
        this.context = context;
        this.type = dialogType;
        createDialog(dialogType);
    }
    public FastDialogBuilder setFullScreen(boolean set){
        fullScreen = set;
        return this;
    }
    private FastDialogBuilder createDialog(Type type){
        dialog =new Dialog(context);
        if (type == Type.PROGRESS){
            dialog.setContentView(R.layout.progress_dialog);
            tvProgress = dialog.findViewById(R.id.wait_text);
        }else if(type == Type.LOGIN) {
            dialog.setContentView(R.layout.login_dialog);
            tvTitle = dialog.findViewById(R.id.login_dialog_title);
            etUsername = dialog.findViewById(R.id.login_dialog_username);
            etPassword = dialog.findViewById(R.id.login_dialog_password);
            Drawable imgPassword = context.getResources().getDrawable(R.drawable.ic_password_black);
            imgPassword.setBounds(0, 0, 45, 45);
            etPassword.setCompoundDrawables(imgPassword, null, null, null);
            Drawable imgUser = context.getResources().getDrawable(R.drawable.ic_user_black);
            imgUser.setBounds(0, 0, 45, 45);
            etUsername.setCompoundDrawables(imgUser, null, null, null);
            btCancel = dialog.findViewById(R.id.warning_dialog_cancel_bt);
            btOk = dialog.findViewById(R.id.warning_dialog_ok_bt);
            btCancel.setOnClickListener(v -> {
                if (negativeClick != null)
                    negativeClick.onClick(v);
                dialog.dismiss();
            });
            btOk.setOnClickListener(v -> {
                if (positiveClick != null)
                    positiveClick.onClick(v);
                dialog.dismiss();
            });
            btOk.setBackground(getShape());
            btCancel.setBackground(getShape());
        }else if(type == Type.FOLDER){
            dialog.setContentView(R.layout.folder_dialog);
            folderLayout = dialog.findViewById(R.id.folder_layout);
            rvFolder = dialog.findViewById(R.id.folder_recylerview);
            folderAdapter = new FolderAdapter(context,fullScreen);
            rvFolder.setLayoutManager(new GridLayoutManager(context,5));
            rvFolder.setAdapter(folderAdapter);

        }else{
            dialog.setContentView(R.layout.warning_dialog);
            tvTitle = dialog.findViewById(R.id.warning_dialog_title);
            lawWarning = dialog.findViewById(R.id.warning_dialog_animation);
            tvWarning = dialog.findViewById(R.id.warning_dialog_text);
            etWarning = dialog.findViewById(R.id.warning_dialog_et);
            etWarningDecimal = dialog.findViewById(R.id.warning_dialog_et_decimal);
            btCancel = dialog.findViewById(R.id.warning_dialog_cancel_bt);
            btOk = dialog.findViewById(R.id.warning_dialog_ok_bt);
            btCancel.setOnClickListener(v -> {
                if(negativeClick != null)
                    negativeClick.onClick(v);
                dialog.dismiss();
            });
            btOk.setOnClickListener(v -> {
                if(positiveClick != null)
                    positiveClick.onClick(v);
                dialog.dismiss();
            });
            btOk.setBackground(getShape());
            btCancel.setBackground(getShape());

            tvTitle.setVisibility(View.VISIBLE);
            lawWarning.setVisibility(View.VISIBLE);
            tvWarning.setVisibility(View.GONE);
            etWarning.setVisibility(View.GONE);
            etWarningDecimal.setVisibility(View.GONE);
            btCancel.setVisibility(View.GONE);
            btOk.setVisibility(View.GONE);
        }
        return this;
    }

    private GradientDrawable getShape(){
        GradientDrawable shape =  new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        //shape.setCornerRadii(new float[] { 20,20,20,20,20,20,20,20 });
        shape.setCornerRadius(20);
        if (type == Type.INFO){
            tvTitle.setBackgroundColor(ContextCompat.getColor(context,R.color.info));
            tvTitle.setText(context.getResources().getString(R.string.info));
            shape.setColor(ContextCompat.getColor(context,R.color.info));
            shape.setStroke(3, ContextCompat.getColor(context,R.color.info));
        }else if (type == Type.ERROR){
            tvTitle.setBackgroundColor(ContextCompat.getColor(context,R.color.error));
            tvTitle.setText(context.getResources().getString(R.string.error));
            shape.setColor(ContextCompat.getColor(context,R.color.error));
            shape.setStroke(3, ContextCompat.getColor(context,R.color.error));
        }else  if (type == Type.WARNING){
            tvTitle.setBackgroundColor(ContextCompat.getColor(context,R.color.warning));
            tvTitle.setText(context.getResources().getString(R.string.warning));
            shape.setColor(ContextCompat.getColor(context,R.color.warning));
            shape.setStroke(3, ContextCompat.getColor(context,R.color.warning));
        }else{
            tvTitle.setBackgroundColor(ContextCompat.getColor(context,R.color.primary));
            tvTitle.setText(context.getResources().getString(R.string.dialog));
            shape.setColor(ContextCompat.getColor(context,R.color.primary));
            shape.setStroke(3, ContextCompat.getColor(context,R.color.primary));
       }

        return shape;
    }
    public FastDialogBuilder changeColor(int colorItem,int colorItemText,int colorText){
        if (folderLayout != null) {
            folderLayout.setBackgroundColor(colorItem);
        }else{
            tvTitle.setBackgroundColor(colorItem);
            tvTitle.setTextColor(colorItemText);
            if (type != Type.LOGIN){
                tvWarning.setTextColor(colorText);
            }
            GradientDrawable shape =  new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            //shape.setCornerRadii(new float[] { 20,20,20,20,20,20,20,20 });
            shape.setCornerRadius(20);
            shape.setColor(colorItem);
            shape.setStroke(3, colorItem);

            btCancel.setBackground(shape);
            btOk.setBackground(shape);
            btCancel.setTextColor(colorItemText);
            btOk.setTextColor(colorItemText);
        }
        return this;
    }
    public FastDialogBuilder setActiveButtons(List<FolderButton> buttonList){
        folderAdapter.setActiveButtons(buttonList);
        folderAdapter.notifyDataSetChanged();
        return this;
    }
    public FastDialogBuilder onClickListener(CustomItemClickListener listener){
        folderAdapter.setClickListener(listener);
        folderAdapter.notifyDataSetChanged();
        return this;
    }
    public FastDialogBuilder progressText(String progressString){
        tvProgress.setText(progressString);
        return this;
    }
    public FastDialogBuilder setAnimation(Animations animation){
        if (animation == Animations.SLIDE_LEFT){
            dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_slide_left;
        }else if(animation == Animations.SLIDE_RIGHT){
            dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_slide_right;
        }else if(animation == Animations.SLIDE_TOP){
            dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_slide_top;
        }else if(animation == Animations.SLIDE_BOTTOM){
            dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_slide_bottom;
        }else if(animation == Animations.FADE_IN){
            dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_fade_in;
        }else if(animation == Animations.GROW_IN){
            dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_grow_in;
        }
        return this;
    }
    public FastDialogBuilder setPosition(Positions position){
        if (position == Positions.CENTER){
            dialog.getWindow().setGravity(Gravity.CENTER);
        }else if (position == Positions.LEFT){
            dialog.getWindow().setGravity(Gravity.START);
        }else if (position == Positions.RIGHT){
            dialog.getWindow().setGravity(Gravity.END);
        }else if (position == Positions.TOP){
            dialog.getWindow().setGravity(Gravity.TOP);
        }else if (position == Positions.BOTTOM){
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
        return this;
    }
    public FastDialogBuilder setTitleText(String title){
        tvTitle.setText(title);
        return this;
    }
    public FastDialogBuilder hideTitle(){
        tvTitle.setVisibility(View.GONE);
        return this;
    }
    public FastDialogBuilder hideIcon(){
        lawWarning.setVisibility(View.GONE);
        return this;
    }
    public FastDialogBuilder setText(String text){
        tvWarning.setVisibility(View.VISIBLE);
        tvWarning.setText(text);
        return this;
    }
    public FastDialogBuilder setHint(String hint){
        etWarning.setVisibility(View.VISIBLE);
        etWarning.setHint(hint);
        etWarningDecimal.setHint(hint);
        return this;
    }
    public FastDialogBuilder loginWithEmail(){
        etUsername.setHint(context.getResources().getString(R.string.email));
        Drawable img = context.getResources().getDrawable( R.drawable.ic_email_black );
        img.setBounds( 0, 0, 45, 45 );
        etUsername.setCompoundDrawables(img,null,null,null);
        return this;
    }
    public FastDialogBuilder negativeText(String negative){
        btCancel.setVisibility(View.VISIBLE);
        btCancel.setText(negative);
        return this;
    }
    public FastDialogBuilder positiveText(String positive){
        btOk.setVisibility(View.VISIBLE);
        btOk.setText(positive);
        return this;
    }
    public FastDialogBuilder decimalEditText(){
        etWarning.setVisibility(View.GONE);
        etWarningDecimal.setVisibility(View.VISIBLE);
        isDecimal = true;
        return this;
    }
    public void positiveClickListener(PositiveClick click){
        btOk.setVisibility(View.VISIBLE);
        positiveClick = click;
    }
    public void negativeClickListener(NegativeClick click){
        btCancel.setVisibility(View.VISIBLE);
        negativeClick = click;
    }
    public void setDismissListener(final DismissListener dismissListener){
        dialog.setOnDismissListener(dialog -> dismissListener.onDismiss(dialog));
    }
    public FastDialogBuilder cancelable(boolean bool){
        dialog.setCancelable(bool);
        return this;
    }

    public FastDialogBuilder privateEditText(){
        etWarning.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etWarningDecimal.setTransformationMethod(PasswordTransformationMethod.getInstance());
        return this;
    }
    public FastDialogBuilder setInputText(String str){
        if (!isDecimal){
            etWarning.setText(str);
            etWarning.setSelection(etWarning.getText().length());
        }
        else{
            etWarningDecimal.setText(str);
            etWarningDecimal.setSelection(etWarningDecimal.getText().length());
        }

        return this;
    }

    public FastDialogBuilder setTextMaxLenght(int lenght){
        etWarning.setFilters(new InputFilter[] { new InputFilter.LengthFilter(lenght) });
        etWarningDecimal.setFilters(new InputFilter[] { new InputFilter.LengthFilter(lenght) });
        return this;
    }
    public FastDialog create(){
        if (fullScreen){
            WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
            lWindowParams.copyFrom(getDialog().getWindow().getAttributes());
            lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lWindowParams);
        }
        return new FastDialog(this);
    }
    public Dialog getDialog(){
        return dialog;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
