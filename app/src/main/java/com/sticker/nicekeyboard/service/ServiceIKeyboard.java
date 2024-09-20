package com.sticker.nicekeyboard.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;


import com.sticker.nicekeyboard.R;
import com.sticker.nicekeyboard.custom.CandidateView;
import com.sticker.nicekeyboard.custom.CustomAKeyBoard;
import com.sticker.nicekeyboard.custom.DrawableFromPathTask;
import com.sticker.nicekeyboard.custom.EmojiCharItemClicked;
import com.sticker.nicekeyboard.custom.EmojiClickCallback;
import com.sticker.nicekeyboard.custom.EmojiObject;
import com.sticker.nicekeyboard.custom.IconPageIndicator;
import com.sticker.nicekeyboard.custom.MethodUltilts;
import com.sticker.nicekeyboard.custom.MyKeyboard;
import com.sticker.nicekeyboard.custom.MyKeyboardView;
import com.sticker.nicekeyboard.custom.Prefrences;
import com.sticker.nicekeyboard.custom.SearchBox;
import com.sticker.nicekeyboard.custom.ShareTheme;
import com.sticker.nicekeyboard.custom.UltilData;
import com.sticker.nicekeyboard.util.Constants;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;


public class ServiceIKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener, View.OnClickListener, SpellCheckerSession.SpellCheckerSessionListener, CustomAKeyBoard.CallBack {
    int NOT_A_LENGTH;
    String TAG = "SERVICE_IKEYBOARD";


    boolean f62a;
    private AudioManager audioManager;


    private Drawable f63d;
    private String data;
    private EmojiCharFragmentPagerAdapter emojiCharPagerAdapter;
    private EmojiFragmentPagerAdapter emojiFragmentPagerAdapter;
    private GridView grvImage;
    private GridView grvMakeEmoji;


    private InputConnection f64ic;
    private ImageView imgDeleteEmoji;
    private ImageView imgEmoji;
    private ImageView imgEmoticons;
    private ImageView imgGallery;
    private ImageView imgMakeEmoji;
    private ImageView imgaddEmoji;
    private boolean isEmoticons;
    private boolean isText;
    private MyKeyboard keyboard;
    private MyKeyboard keyboardNumber;


    private MyKeyboardView keyboardView;
    private ListImageAdapter lisEmojiAdapter;
    private List<EmojiObject> listBody;
    private List<EmojiObject> listCouple;
    private List<EmojiObject> listEmojiChar1;
    private List<EmojiObject> listEmojiChar2;
    private List<EmojiObject> listEmojiChar3;
    private List<EmojiObject> listEmojiChar4;
    private List<EmojiObject> listEmotcon;
    private List<EmojiObject> listFlag;
    private List<EmojiObject> listHouse;
    private List<String> listImage;
    private ListImageAdapter listImageAdapter;
    private List<String> listMakeEmoji;
    private List<EmojiObject> listNature;
    private List<EmojiObject> listObject;
    private List<EmojiObject> listPack;
    private List<EmojiObject> listPeople;
    private List<EmojiObject> listSport;
    private List<EmojiObject> listSymbols;
    private List<View> listTab;
    private RelativeLayout lnlImage;
    private LinearLayout lnlKeyboardGenneral;
    private CandidateView mCandidateView;
    private CompletionInfo[] mCompletions;
    private int mLastDisplayWidth;
    private MyKeyboard mQwertyKeyboard;
    private MyKeyboard mQwertyKeyboardUp;
    private SpellCheckerSession mScs;
    private List<String> mSuggestions;
    private MyKeyboard mSymbolsKeyboard;
    private MyKeyboard mSymbolsShiftedKeyboard;
    private MethodUltilts methodUltilts;
    private RelativeLayout rltEmoji;
    private LinearLayout rltParent;
    private RelativeLayout rltViewPager;
    private SharedPreferences sharedPreferences;
    private IconPageIndicator tabLayout;
    private String text;
    private CountDownTimer timer;
    private TextView tvBackABC;
    private TextView tvChar;
    private UltilData ultilData;
    private ViewPager vpEmoji;
    private boolean caps = false;
    private boolean emoji = false;
    private EmojiCharItemClicked emojiCharItemClickedCallback = new EmojiCharItemClicked() {
        @Override
        public void emojiCharItemClickedListenner(String str) {
            if (ServiceIKeyboard.this.sharedPreferences.getBoolean(Constants.ENABLE_SOUND, false)) {
                if (ServiceIKeyboard.this.audioManager.isSpeakerphoneOn()) {
                    ServiceIKeyboard.this.audioManager.stopBluetoothSco();
                }
                ServiceIKeyboard.this.playKeyboard(-13);
            }
            ServiceIKeyboard.this.getCurrentInputConnection().commitText(String.valueOf(str), 1);
        }
    };
    private EmojiClickCallback emojiClickCallback = new EmojiClickCallback() {
        @Override
        public void emojiClickedListenner(String str, String str2) {
            if (ServiceIKeyboard.this.sharedPreferences.getBoolean(Constants.ENABLE_SOUND, false)) {
                ServiceIKeyboard.this.playKeyboard(13);
            }
            if (str2.contains("emoticons")) {
                try {
                    ServiceIKeyboard serviceIKeyboard = ServiceIKeyboard.this;
                    MethodUltilts methodUltilts = ServiceIKeyboard.this.methodUltilts;
                    ServiceIKeyboard serviceIKeyboard2 = ServiceIKeyboard.this;
                    serviceIKeyboard.passImage(methodUltilts.copyFileFromAssets(serviceIKeyboard2, str2, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "temp.png").getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String str3 = str.split(".png")[0].split("emoji_")[1];
                if (str3.contains("_")) {
                    InputConnection currentInputConnection = ServiceIKeyboard.this.getCurrentInputConnection();
                    int intValue = Integer.decode("0x" + str3.split("_")[0]).intValue();
                    int intValue2 = Integer.decode("0x" + str3.split("_")[1]).intValue();
                    currentInputConnection.commitText(new String(Character.toChars(intValue)), 1);
                    currentInputConnection.commitText(new String(Character.toChars(intValue2)), 1);
                    return;
                }
                InputConnection currentInputConnection2 = ServiceIKeyboard.this.getCurrentInputConnection();
                currentInputConnection2.commitText(new String(Character.toChars(Integer.decode("0x" + str3).intValue())), 1);
            }
        }
    };
    private boolean flagVoid = false;
    private StringBuilder mComposing = new StringBuilder();
    private boolean mPredictionOn = false;
    private BroadcastReceiver setFonts = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MyKeyboardView myKeyboardView = ServiceIKeyboard.this.keyboardView;
            AssetManager assets = ServiceIKeyboard.this.getAssets();
            myKeyboardView.setTypeface(Typeface.createFromAsset(assets, "fonts/" + intent.getStringExtra("setFonts")));
        }
    };
    private BroadcastReceiver setupKeyboard = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                ServiceIKeyboard.this.setThemeForKeyboard();

            } catch (Exception e) {

            }
        }
    };
    private BroadcastReceiver setupPopUp = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("setup_popup").equalsIgnoreCase("on")) {
                ServiceIKeyboard.this.keyboardView.setPreviewEnabled(true);
            } else {
                ServiceIKeyboard.this.keyboardView.setPreviewEnabled(false);
            }
        }
    };
    private View.OnLongClickListener speakHoldListener = new View.OnLongClickListener() {


        class AnonymousClass1 extends CountDownTimer {
            private View val$view;


            AnonymousClass1(long j, long j2, View view) {
                super(1000, 50);
                this.val$view = view;
            }

            @Override
            public void onTick(long j) {
                if (this.val$view == ServiceIKeyboard.this.imgDeleteEmoji) {
                    ServiceIKeyboard.this.exeDelete();
                } else {
                    ServiceIKeyboard.this.exeSpace();
                }
            }

            @Override
            public void onFinish() {
                try {
                    ServiceIKeyboard.this.timer.start();
                } catch (Exception unused) {
                }
            }
        }

        @Override
        public boolean onLongClick(View view) {
            ServiceIKeyboard.this.f62a = true;
            ServiceIKeyboard.this.timer = new AnonymousClass1(1000, 50, view).start();
            return true;
        }
    };
    private View.OnTouchListener speakTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            view.onTouchEvent(motionEvent);
            if (motionEvent.getAction() == 1 && ServiceIKeyboard.this.f62a) {
                ServiceIKeyboard.this.f62a = false;
                ServiceIKeyboard.this.timer.cancel();
            }
            return true;
        }
    };
    private String statusPosition = "";
    private String str = "";
    private int tabEmoji = 1;
    private Object[] tabTitles = {Integer.valueOf((int) R.mipmap.emoji_1f60a), Integer.valueOf((int) R.mipmap.emoji_1f46e), Integer.valueOf((int) R.mipmap.emoji_1f44d), Integer.valueOf((int) R.mipmap.emoji_1f31e), Integer.valueOf((int) R.mipmap.emoji_1f6b5_1f3ff), Integer.valueOf((int) R.mipmap.emoji_1f1fb_1f1f3), Integer.valueOf((int) R.mipmap.emoji_1f492), Integer.valueOf((int) R.mipmap.emoji_1f696), Integer.valueOf((int) R.mipmap.emoji_2648)};
    private Object[] titleCharEmoji = {"(^_^)", "(>‿◠)✌", "♥LOVE♥", "✌Enter✌"};
    private String typeKeyboard = "text";
    int count = 0;
    private ImageView img_previous, img_next;

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }


    class getEmoji extends AsyncTask<Void, Void, Void> {
        getEmoji() {
        }

        private Void doInBackground$10299ca() {
            ServiceIKeyboard.this.listFlag = new ArrayList();
            ServiceIKeyboard.this.listHouse = new ArrayList();
            ServiceIKeyboard.this.listPeople = new ArrayList();
            ServiceIKeyboard.this.listObject = new ArrayList();
            ServiceIKeyboard.this.listNature = new ArrayList();
            ServiceIKeyboard.this.listSymbols = new ArrayList();
            ServiceIKeyboard.this.listSport = new ArrayList();
            ServiceIKeyboard.this.listBody = new ArrayList();
            ServiceIKeyboard.this.listCouple = new ArrayList();
            ServiceIKeyboard.this.listEmojiChar1 = new ArrayList();
            ServiceIKeyboard.this.listEmojiChar2 = new ArrayList();
            ServiceIKeyboard.this.listEmojiChar3 = new ArrayList();
            ServiceIKeyboard.this.listEmojiChar4 = new ArrayList();
            ServiceIKeyboard.this.listPack = new ArrayList();
            ServiceIKeyboard.this.listEmotcon = new ArrayList();
            ServiceIKeyboard.this.listMakeEmoji = new ArrayList();
            ServiceIKeyboard.this.listPeople = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/people");
            ServiceIKeyboard.this.listFlag = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/flag");
            ServiceIKeyboard.this.listHouse = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/house");
            ServiceIKeyboard.this.listObject = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/objects");
            ServiceIKeyboard.this.listNature = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/nature");
            ServiceIKeyboard.this.listSymbols = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/symbols");
            ServiceIKeyboard.this.listSport = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/sport");
            ServiceIKeyboard.this.listBody = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/body");
            ServiceIKeyboard.this.listCouple = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/couple");
            ServiceIKeyboard.this.listEmotcon = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/emoticons");
            ServiceIKeyboard.this.listPack = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/pack");
            ServiceIKeyboard.this.listEmojiChar1 = ServiceIKeyboard.this.ultilData.setEmojiChart1();
            ServiceIKeyboard.this.listEmojiChar2 = ServiceIKeyboard.this.ultilData.setEmojiChart2();
            ServiceIKeyboard.this.listEmojiChar3 = ServiceIKeyboard.this.ultilData.setEmojiChart3();
            ServiceIKeyboard.this.listEmojiChar4 = ServiceIKeyboard.this.ultilData.setEmojiChart4();
            ServiceIKeyboard.this.listMakeEmoji = ServiceIKeyboard.this.methodUltilts.loadImgFromSdCard();
            return null;
        }

        public void onPostExecute(Void r1) {
            super.onPostExecute(r1);
        }

        public Void doInBackground(Void... voidArr) {
            ServiceIKeyboard.this.listFlag = new ArrayList();
            ServiceIKeyboard.this.listHouse = new ArrayList();
            ServiceIKeyboard.this.listPeople = new ArrayList();
            ServiceIKeyboard.this.listObject = new ArrayList();
            ServiceIKeyboard.this.listNature = new ArrayList();
            ServiceIKeyboard.this.listSymbols = new ArrayList();
            ServiceIKeyboard.this.listSport = new ArrayList();
            ServiceIKeyboard.this.listBody = new ArrayList();
            ServiceIKeyboard.this.listCouple = new ArrayList();
            ServiceIKeyboard.this.listEmojiChar1 = new ArrayList();
            ServiceIKeyboard.this.listEmojiChar2 = new ArrayList();
            ServiceIKeyboard.this.listEmojiChar3 = new ArrayList();
            ServiceIKeyboard.this.listEmojiChar4 = new ArrayList();
            ServiceIKeyboard.this.listPack = new ArrayList();
            ServiceIKeyboard.this.listEmotcon = new ArrayList();
            ServiceIKeyboard.this.listMakeEmoji = new ArrayList();
            ServiceIKeyboard.this.listPeople = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/people");
            ServiceIKeyboard.this.listFlag = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/flag");
            ServiceIKeyboard.this.listHouse = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/house");
            ServiceIKeyboard.this.listObject = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/objects");
            ServiceIKeyboard.this.listNature = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/nature");
            ServiceIKeyboard.this.listSymbols = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/symbols");
            ServiceIKeyboard.this.listSport = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/sport");
            ServiceIKeyboard.this.listBody = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/body");
            ServiceIKeyboard.this.listCouple = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/couple");
            ServiceIKeyboard.this.listEmotcon = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/emoticons");
            ServiceIKeyboard.this.listPack = ServiceIKeyboard.this.methodUltilts.getAllNameImageByType(ServiceIKeyboard.this, "emoji/pack");
            ServiceIKeyboard.this.listEmojiChar1 = ServiceIKeyboard.this.ultilData.setEmojiChart1();
            ServiceIKeyboard.this.listEmojiChar2 = ServiceIKeyboard.this.ultilData.setEmojiChart2();
            ServiceIKeyboard.this.listEmojiChar3 = ServiceIKeyboard.this.ultilData.setEmojiChart3();
            ServiceIKeyboard.this.listEmojiChar4 = ServiceIKeyboard.this.ultilData.setEmojiChart4();
            ServiceIKeyboard.this.listMakeEmoji = ServiceIKeyboard.this.methodUltilts.loadImgFromSdCard();
            return null;
        }
    }

    public String escapeUnicodeText(String str) throws IOException {
        StringBuilder sb = new StringBuilder(str.length());
        Formatter formatter = new Formatter(sb);
        char[] charArray = str.toCharArray();
        for (char c : charArray) {
            if (c < 128) {
                sb.append(c);
            } else {
                formatter.format("\\u%04x", Integer.valueOf(c));
            }
        }
        return sb.toString();
    }

    public String getUnicodeString(String str) {
        try {
            return new String(str.getBytes("UTF8"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (this.mQwertyKeyboard != null) {
            int maxWidth = getMaxWidth();
            if (maxWidth != this.mLastDisplayWidth) {
                this.mLastDisplayWidth = maxWidth;
            } else {
                return;
            }
        }
        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        this.methodUltilts = new MethodUltilts(this);
        this.listImage = this.methodUltilts.getAllShownImagesPath(this);
        this.listTab = new ArrayList();
        this.ultilData = new UltilData(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(this.setupKeyboard, new IntentFilter("custom_name"));
        LocalBroadcastManager.getInstance(this).registerReceiver(this.setupPopUp, new IntentFilter("setup_popup"));
        LocalBroadcastManager.getInstance(this).registerReceiver(this.setFonts, new IntentFilter("setFonts"));
        this.mScs = ((TextServicesManager) getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE)).newSpellCheckerSession(null, null, this, true);
    }

    private void initKeyboard() {
        String stringPref = Prefrences.getStringPref(this, "selectedLang");
        if (stringPref.equals("Hindi")) {
            this.mQwertyKeyboard = new MyKeyboard(this, R.xml.hindi);
            this.mQwertyKeyboardUp = new MyKeyboard(this, R.xml.hindi_up);
            this.mSymbolsKeyboard = new MyKeyboard(this, R.xml.hindi_symbols);
            this.mSymbolsShiftedKeyboard = new MyKeyboard(this, R.xml.symbols_shift);
            this.keyboardNumber = new MyKeyboard(this, R.xml.hindi_number);
        } else if (stringPref.equals("Gujarati")) {
            this.mQwertyKeyboard = new MyKeyboard(this, R.xml.guj);
            this.mQwertyKeyboardUp = new MyKeyboard(this, R.xml.guj_up);
            this.mSymbolsKeyboard = new MyKeyboard(this, R.xml.guj_symbols);
            this.mSymbolsShiftedKeyboard = new MyKeyboard(this, R.xml.symbols_shift);
            this.keyboardNumber = new MyKeyboard(this, R.xml.guj_number);
        } else {
            this.mQwertyKeyboard = new MyKeyboard(this, R.xml.qwerty);
            this.mQwertyKeyboardUp = new MyKeyboard(this, R.xml.qwerty_up);
            this.mSymbolsKeyboard = new MyKeyboard(this, R.xml.symbols);
            this.mSymbolsShiftedKeyboard = new MyKeyboard(this, R.xml.symbols_shift);
            this.keyboardNumber = new MyKeyboard(this, R.xml.number);
        }
    }

    @Override
    public View onCreateCandidatesView() {
        this.mCandidateView = new CandidateView(this);
        this.mCandidateView.setService(this);
        return this.mCandidateView;
    }

    @Override
    public void onInitializeInterface() {
        super.onInitializeInterface();
        new getEmoji().execute(new Void[0]);
        initKeyboard();
    }

    @Override
    public View onCreateInputView() {
        this.rltParent = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_keyboard, (ViewGroup) null);
        initKeyboard();
        this.keyboardView = (MyKeyboardView) this.rltParent.findViewById(R.id.keyboard);
        this.keyboardView.setOnKeyboardActionListener(this);
        this.keyboardView.setPreviewEnabled(false);
        this.keyboardView.setKeyboard(this.keyboard);
        this.grvImage = (GridView) this.rltParent.findViewById(R.id.grvImage);
        this.grvMakeEmoji = (GridView) this.rltParent.findViewById(R.id.grvMakeEmoji);
        this.lnlImage = (RelativeLayout) this.rltParent.findViewById(R.id.lnlImage);
        this.tvBackABC = (TextView) this.rltParent.findViewById(R.id.tvBackABC);
        this.imgDeleteEmoji = (ImageView) this.rltParent.findViewById(R.id.imgDeleteEmoji);
        this.lnlKeyboardGenneral = (LinearLayout) this.rltParent.findViewById(R.id.lnlKeyboardGenneral);
        this.imgEmoji = (ImageView) this.rltParent.findViewById(R.id.imgEmoji);
        this.imgMakeEmoji = (ImageView) this.rltParent.findViewById(R.id.imgMakeEmoji);
        this.imgaddEmoji = (ImageView) this.rltParent.findViewById(R.id.imgaddEmoji);
        this.tvChar = (TextView) this.rltParent.findViewById(R.id.tvChar);
        this.rltEmoji = (RelativeLayout) this.rltParent.findViewById(R.id.rltEmoji);
        this.img_previous = (ImageView) this.rltParent.findViewById(R.id.img_previous);
        this.img_next = (ImageView) this.rltParent.findViewById(R.id.img_next);
        this.vpEmoji = (ViewPager) this.rltParent.findViewById(R.id.vpEmoji);
        this.rltViewPager = (RelativeLayout) this.rltParent.findViewById(R.id.rltViewPager);
        this.sharedPreferences = getSharedPreferences(Constants.IKEYBOARD, 0);
        this.emojiFragmentPagerAdapter = new EmojiFragmentPagerAdapter(this, this.tabTitles, this.listPeople, this.emojiClickCallback, false);
        this.vpEmoji.setAdapter(this.emojiFragmentPagerAdapter);
        this.tabLayout = (IconPageIndicator) this.rltParent.findViewById(R.id.sliding_tabs);
        this.tabLayout.setViewPager(this.vpEmoji, this.isText, 9);
        this.listTab.add(this.tvBackABC);
        this.listTab.add(this.imgEmoji);
        this.listTab.add(this.tvChar);
        this.listTab.add(this.imgDeleteEmoji);
        this.listTab.add(this.imgMakeEmoji);
        if (this.listMakeEmoji != null && this.listMakeEmoji.size() > 0) {
            this.lisEmojiAdapter = new ListImageAdapter(this, this.listMakeEmoji);
            this.grvMakeEmoji.setAdapter((ListAdapter) this.lisEmojiAdapter);
        }
        if (this.listImage != null && this.listImage.size() > 0) {
            this.listImageAdapter = new ListImageAdapter(this, this.listImage);
            this.grvImage.setAdapter((ListAdapter) this.listImageAdapter);
        }
        this.grvImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                ServiceIKeyboard.this.passImage((String) adapterView.getItemAtPosition(i));
            }
        });
        this.tvBackABC.setOnClickListener(this);
        this.imgEmoji.setOnClickListener(this);
        this.tvChar.setOnClickListener(this);
        this.imgaddEmoji.setOnClickListener(this);
        this.imgDeleteEmoji.setOnClickListener(this);
        this.imgMakeEmoji.setOnClickListener(this);
        this.imgDeleteEmoji.setOnLongClickListener(this.speakHoldListener);
        this.imgDeleteEmoji.setOnTouchListener(this.speakTouchListener);
        this.vpEmoji.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i) {
            }

            @Override
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    if (ServiceIKeyboard.this.isText) {
                        ServiceIKeyboard.this.emojiCharPagerAdapter.setDataChanges(ServiceIKeyboard.this.listEmojiChar1);
                    } else if (ServiceIKeyboard.this.isEmoticons) {
                        ServiceIKeyboard.this.emojiFragmentPagerAdapter.setDataChanges(ServiceIKeyboard.this.listEmotcon, ServiceIKeyboard.this.isEmoticons);
                    } else {
                        ServiceIKeyboard.this.emojiFragmentPagerAdapter.setDataChanges(ServiceIKeyboard.this.listPeople, ServiceIKeyboard.this.isEmoticons);
                    }
                } else if (i == 1) {
                    if (ServiceIKeyboard.this.isText) {
                        ServiceIKeyboard.this.emojiCharPagerAdapter.setDataChanges(ServiceIKeyboard.this.listEmojiChar2);
                    } else {
                        ServiceIKeyboard.this.emojiFragmentPagerAdapter.setDataChanges(ServiceIKeyboard.this.listCouple, ServiceIKeyboard.this.isEmoticons);
                    }
                } else if (i == 2) {
                    if (ServiceIKeyboard.this.isText) {
                        ServiceIKeyboard.this.emojiCharPagerAdapter.setDataChanges(ServiceIKeyboard.this.listEmojiChar3);
                    } else {
                        ServiceIKeyboard.this.emojiFragmentPagerAdapter.setDataChanges(ServiceIKeyboard.this.listBody, ServiceIKeyboard.this.isEmoticons);
                    }
                } else if (i == 3) {
                    if (ServiceIKeyboard.this.isText) {
                        ServiceIKeyboard.this.emojiCharPagerAdapter.setDataChanges(ServiceIKeyboard.this.listEmojiChar4);
                    } else {
                        ServiceIKeyboard.this.emojiFragmentPagerAdapter.setDataChanges(ServiceIKeyboard.this.listNature, ServiceIKeyboard.this.isEmoticons);
                    }
                } else if (i == 4) {
                    ServiceIKeyboard.this.emojiFragmentPagerAdapter.setDataChanges(ServiceIKeyboard.this.listSport, ServiceIKeyboard.this.isEmoticons);
                } else if (i == 5) {
                    ServiceIKeyboard.this.emojiFragmentPagerAdapter.setDataChanges(ServiceIKeyboard.this.listFlag, ServiceIKeyboard.this.isEmoticons);
                } else if (i == 6) {
                    ServiceIKeyboard.this.emojiFragmentPagerAdapter.setDataChanges(ServiceIKeyboard.this.listHouse, ServiceIKeyboard.this.isEmoticons);
                } else if (i == 7) {
                    ServiceIKeyboard.this.emojiFragmentPagerAdapter.setDataChanges(ServiceIKeyboard.this.listObject, ServiceIKeyboard.this.isEmoticons);
                } else if (i == 8) {
                    ServiceIKeyboard.this.emojiFragmentPagerAdapter.setDataChanges(ServiceIKeyboard.this.listSymbols, ServiceIKeyboard.this.isEmoticons);
                }
            }
        });
        this.lnlImage.getLayoutParams().height = this.keyboard.getHeight() + this.methodUltilts.dip2px(this, 10.0f);
        setThemeForKeyboard();

        //Add
        if (this.keyboardView != null) {
            if (this.count % 2 == 0) {
                this.keyboard = this.mQwertyKeyboardUp;
            } else {
                this.keyboard = this.mQwertyKeyboard;
            }
            this.keyboardView.setKeyboard(this.keyboard);
            this.keyboard.setShifted(this.caps);
            this.keyboardView.invalidateAllKeys();
            handleShift();
        }
        //end

        //Add v1.1.5 09/03/2023
        img_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = vpEmoji.getCurrentItem();
                if (currentItem > 0)
                    vpEmoji.setCurrentItem(currentItem - 1);
            }
        });
        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = vpEmoji.getCurrentItem();
                if (currentItem < 8)
                    vpEmoji.setCurrentItem(currentItem + 1);
            }
        });
        //end
        return this.rltParent;
    }

    public void passImage(String str) {
        try {
            getApplicationContext().startActivity(createIntent(getApplicationContext(), getCurrentInputBinding(), str));
        } catch (ActivityNotFoundException e) {
            Toast makeText = Toast.makeText(getApplicationContext(), getResources().getString(R.string.share_image_fail), Toast.LENGTH_SHORT);
            makeText.setGravity(17, 0, 10);
            getCurrentInputConnection().commitText(String.valueOf(str), 1);
            makeText.show();
            e.printStackTrace();
        }
    }

    @SuppressLint({"WrongConstant"})
    public Intent createIntent(Context context, InputBinding inputBinding, String str) {
        String[] packagesForUid = context.getPackageManager().getPackagesForUid(inputBinding.getUid());
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addFlags(268435456);
        intent.addFlags(32768);
        intent.setType("image/*");
        intent.setPackage(packagesForUid[0]);
        intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(str)));
        return intent;
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        if (!(intent == null || this.keyboardView == null)) {
            this.data = intent.getStringExtra(SearchBox.DATA_VOID);
            this.flagVoid = true;
        }
        return super.onStartCommand(intent, i, i2);
    }

    @Override
    @RequiresApi(api = 16)
    public void onStartInputView(EditorInfo editorInfo, boolean z) {
        super.onStartInputView(editorInfo, z);
        this.mComposing.setLength(0);
        updateCandidates();
        setInputView(onCreateInputView());
    }

    public void playKeyboard(int i) {
        if (i == -5) {
            this.audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE, 0.8f);
        } else if (i == 10) {
            this.audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN, 0.8f);
        } else if (i != 32) {
            this.audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD, 0.8f);
        } else {
            this.audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR, 0.8f);
        }
    }

    public Drawable getDrawableFromPath(String str) {
        if (str.startsWith("http")) { // from internet
            try {
                URL url = new URL(str);
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return new BitmapDrawable(image);
            } catch (IOException e) {
                return null;
            }
        } else {
            return new BitmapDrawable(BitmapFactory.decodeFile(str));
        }
    }

    @Override
    @SuppressLint({"WrongConstant"})
    public void onPress(int i) {
        boolean boolPref = Prefrences.getBoolPref(this, "sound_enable");
        boolean boolPref2 = Prefrences.getBoolPref(this, "vibration_enable");


        Log.e("press", "" + i);
        if (boolPref) {
            playKeyboard(i);
        }
        if (boolPref2) {
            ((Vibrator) getSystemService("vibrator")).vibrate(100);
        }
        if (i == 32 || i == 10 || i == -1 || i == -5 || i == -2 || i == -111) {
            this.keyboardView.setPreviewEnabled(false);
        } else if (i == -10) {
            this.keyboardView.setPreviewEnabled(false);
            this.keyboardView.setVisibility(View.GONE);
            this.lnlImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    @RequiresApi(api = 16)
    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        return super.onKeyLongPress(i, keyEvent);

    }

    @Override
    public void onRelease(int i) {
        if (i == -5) {
            this.keyboardView.setPreviewEnabled(false);
        } else {
            this.keyboardView.setPreviewEnabled(true);
        }
    }

    @Override
    @RequiresApi(api = 16)
    public void onFinishInput() {
        super.onFinishInput();
        if (this.keyboardView != null) {
            this.keyboardView.closing();
        }
        this.mComposing.setLength(0);
        updateCandidates();
        setCandidatesViewShown(false);
    }

    @Override
    public boolean onShowInputRequested(int i, boolean z) {
        this.caps = false;
        if (this.keyboardView != null) {
            this.emoji = false;
            this.keyboardView.setKeyboard(this.keyboard);
            this.keyboardView.closing();
        }
        if (this.flagVoid) {
            this.flagVoid = false;
            getCurrentInputConnection().commitText(this.data, 1);
        }
        return super.onShowInputRequested(i, z);
    }

    @Override
    public void onStartInput(EditorInfo editorInfo, boolean z) {
        super.onStartInput(editorInfo, z);
        this.keyboard = this.mQwertyKeyboard;


        int i = editorInfo.imeOptions;
        this.mCompletions = null;
    }

    @Override
    public void onDisplayCompletions(CompletionInfo[] completionInfoArr) {
        this.mCompletions = completionInfoArr;
        if (completionInfoArr == null) {
            setSuggestions(null, false, false);
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (CompletionInfo completionInfo : completionInfoArr) {
            if (completionInfo != null) {
                try {
                    arrayList.add(completionInfo.getText().toString());
                } catch (Exception e) {
                    Log.d(TAG, "onDisplayCompletions: error occur " + e);
                }
            }
        }
        setSuggestions(arrayList, true, true);
    }

    @Override
    @SuppressLint({"WrongConstant"})
    public void onKey(int i, int[] iArr) {

        Log.e("onKey", "" + i);
        this.count++;
        if (this.sharedPreferences.getBoolean(Constants.ENABLE_SOUND, false)) {
            if (this.audioManager.isSpeakerphoneOn()) {
                this.audioManager.stopBluetoothSco();
            }
            playKeyboard(i);
        }
        InputConnection currentInputConnection = getCurrentInputConnection();
        this.text = (String) currentInputConnection.getTextBeforeCursor(99999, 0);
        String str = (String) currentInputConnection.getSelectedText(0);
        switch (i) {
            case MyKeyboardView.KEYCODE_EMOJI_8:
                this.keyboardView.setVisibility(View.GONE);
                this.lnlImage.setVisibility(View.VISIBLE);
                break;
            case -111:
                MicroPhoneClicked();
                break;
            case -5: //onClickDelete
                if (str == null) {
                    currentInputConnection.deleteSurroundingText(1, 0);
                } else {
                    currentInputConnection.setComposingText("", 1);
                }
                break;
            case -4:
                currentInputConnection.sendKeyEvent(new KeyEvent(0, 66));
                break;
            case -2:
                Keyboard keyboard = this.keyboardView.getKeyboard();
                if (keyboard == this.mSymbolsKeyboard || keyboard == this.mSymbolsShiftedKeyboard) {
                    if (this.caps) {
                        setLatinKeyboard(this.mQwertyKeyboardUp);
                        break;
                    } else {
                        setLatinKeyboard(this.mQwertyKeyboard);
                        break;
                    }
                } else {
                    setLatinKeyboard(this.mSymbolsKeyboard);
                    this.mSymbolsKeyboard.setShifted(false);
                    break;
                }

            case -1:
                if (this.count % 2 == 0) {
                    this.keyboard = this.mQwertyKeyboardUp;
                    this.keyboardView.setKeyboard(this.keyboard);
                    this.keyboard.setShifted(this.caps);
                    this.keyboardView.invalidateAllKeys();
                }
                handleShift();
                break;
            case 10:
                sendKey(i);
                break;
            default:
                handleCharacter(i, iArr);
                break;
        }
        this.text = (String) currentInputConnection.getTextBeforeCursor(99999, 0);
    }

    @SuppressLint({"WrongConstant"})
    private void callVoid() {
        Intent intent = new Intent(this, SearchBox.class);
        intent.addFlags(268435456);
        startActivity(intent);
    }

    public void changeEmojiKeyboard(MyKeyboard[] myKeyboardArr, boolean z) {
        this.emoji = true;
        int i = 0;
        int i2 = 0;
        while (i < myKeyboardArr.length && myKeyboardArr[i] != this.keyboardView.getKeyboard()) {
            if (i == myKeyboardArr.length - 1) {
                i2 = i;
            }
            i++;
        }
        if (z) {
            int i3 = i2 - 1;
            if (i3 < 0) {
                this.keyboardView.setKeyboard(myKeyboardArr[myKeyboardArr.length - 1]);
            } else {
                this.keyboardView.setKeyboard(myKeyboardArr[i3]);
            }
        } else {
            int i4 = i2 + 1;
            if (i4 >= myKeyboardArr.length) {
                this.keyboardView.setKeyboard(myKeyboardArr[0]);
            } else {
                this.keyboardView.setKeyboard(myKeyboardArr[i4]);
            }
        }
    }

    private void sendKey(int i) {
        if (i == 10) {
            keyDownUp(66);
        } else if (i < 48 || i > 57) {
            getCurrentInputConnection().commitText(String.valueOf((char) i), 1);
        } else {
            keyDownUp((i - 48) + 7);
        }
    }

    private void keyDownUp(int i) {
        getCurrentInputConnection().sendKeyEvent(new KeyEvent(0, i));
        getCurrentInputConnection().sendKeyEvent(new KeyEvent(1, i));
    }

    private void handleShift() {
        if (this.keyboardView != null) {
            Keyboard keyboard = this.keyboardView.getKeyboard();
            if (this.mQwertyKeyboard == keyboard || this.mQwertyKeyboardUp == keyboard) {
                this.caps = !this.caps;
                if (this.caps) {
                    this.keyboard = this.mQwertyKeyboardUp;
                } else {
                    this.keyboard = this.mQwertyKeyboard;
                }
                this.keyboardView.setKeyboard(this.keyboard);
                this.keyboard.setShifted(this.caps);
                this.keyboardView.invalidateAllKeys();
            } else if (keyboard == this.mSymbolsKeyboard) {
                this.mSymbolsKeyboard.setShifted(true);
                setLatinKeyboard(this.mSymbolsShiftedKeyboard);
                this.mSymbolsShiftedKeyboard.setShifted(true);
            } else if (keyboard == this.mSymbolsShiftedKeyboard) {
                this.mSymbolsShiftedKeyboard.setShifted(false);
                setLatinKeyboard(this.mSymbolsKeyboard);
                this.mSymbolsKeyboard.setShifted(false);
            }
        }
    }

    private void setLatinKeyboard(MyKeyboard myKeyboard) {
        this.keyboardView.setKeyboard(myKeyboard);
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            //return keyEvent.getRepeatCount() == 0 && this.keyboardView != null && this.keyboardView.handleBack();
            if (this.keyboardView != null && this.keyboardView.isShown()) {
                this.keyboardView.closing();
            }
        }
        if (i != 66) {
            return super.onKeyDown(i, keyEvent);
        }
        return false;
    }

    @Override
    public void onText(CharSequence charSequence) {
        InputConnection currentInputConnection = getCurrentInputConnection();
        if (currentInputConnection != null) {
            currentInputConnection.beginBatchEdit();
            if (this.mComposing.length() > 0) {
                commitTyped(currentInputConnection);
            }
            currentInputConnection.commitText(charSequence, 0);
            currentInputConnection.endBatchEdit();
            updateShiftKeyState(getCurrentInputEditorInfo());
        }
    }

    @Override
    public void swipeLeft() {
        handleBackspace();
    }

    private void handleCharacter(int i, int[] iArr) {
        if (isInputViewShown() && this.keyboardView.isShifted()) {
            i = Character.toUpperCase(i);
        }
        if (this.emoji || !this.mPredictionOn) {
            getCurrentInputConnection().commitText(String.valueOf((char) i), 1);
            return;
        }
        this.mComposing.append((char) i);
        getCurrentInputConnection().setComposingText(this.mComposing, 1);
        updateShiftKeyState(getCurrentInputEditorInfo());
        updateCandidates();
    }

    @Override
    @SuppressLint({"WrongConstant"})
    public void onClick(View view) {
        if (view == this.imgMakeEmoji) {
            this.listMakeEmoji = new ArrayList();
            this.listMakeEmoji = this.methodUltilts.loadImgFromSdCard();
            if (this.listMakeEmoji == null || this.listMakeEmoji.size() <= 0) {
                setClick(this.imgMakeEmoji);
                this.grvImage.setVisibility(View.GONE);
                this.rltEmoji.setVisibility(View.GONE);
                this.imgaddEmoji.setVisibility(View.VISIBLE);
                return;
            }
            setClick(this.imgMakeEmoji);
            this.grvImage.setVisibility(View.VISIBLE);
            this.rltEmoji.setVisibility(View.GONE);
            this.imgaddEmoji.setVisibility(View.GONE);
            this.listImageAdapter.setDataChanges(this.listMakeEmoji);
        } else if (view != this.imgaddEmoji) {
            if (view == this.tvBackABC) {
                this.imgaddEmoji.setVisibility(View.GONE);
                this.lnlImage.setVisibility(View.GONE);
                this.keyboardView.setVisibility(View.VISIBLE);
                return;
            }
            if (view == this.imgDeleteEmoji) {
                this.imgaddEmoji.setVisibility(View.GONE);
                this.f64ic = getCurrentInputConnection();
                if (((String) this.f64ic.getSelectedText(0)) != null) {
                    this.f64ic.setComposingText("", 1);
                    return;
                } else if (this.text.length() > 0) {
                    try {
                        if (URLEncoder.encode(this.text.substring(this.text.length() - 1), "UTF-8").equals("%3F")) {
                            this.f64ic.deleteSurroundingText(2, 0);
                            return;
                        } else {
                            this.f64ic.deleteSurroundingText(1, 0);
                            return;
                        }
                    } catch (UnsupportedEncodingException unused) {
                        this.f64ic.deleteSurroundingText(1, 0);
                    }
                } else {
                    this.f64ic.deleteSurroundingText(1, 0);
                    return;
                }
            } else if (view == this.imgEmoji) {
                this.imgaddEmoji.setVisibility(View.GONE);
                setClick(this.imgEmoji);
                this.isEmoticons = false;
                this.isText = false;
                this.emojiFragmentPagerAdapter = new EmojiFragmentPagerAdapter(this, this.tabTitles, this.listPeople, this.emojiClickCallback, this.isEmoticons);
                this.vpEmoji.setAdapter(this.emojiFragmentPagerAdapter);
                this.tabLayout.setViewPager(this.vpEmoji, this.isText, 9);
                this.grvImage.setVisibility(View.GONE);
                this.rltEmoji.setVisibility(View.VISIBLE);
                return;
            } else if (view == this.tvChar) {
                this.imgaddEmoji.setVisibility(View.GONE);
                this.isText = true;
                setClick(this.tvChar);
                this.emojiCharPagerAdapter = new EmojiCharFragmentPagerAdapter(this, this.titleCharEmoji, this.listEmojiChar1, this.emojiCharItemClickedCallback);
                this.vpEmoji.setAdapter(this.emojiCharPagerAdapter);
                this.tabLayout.setViewPager(this.vpEmoji, this.isText, 4);
                this.grvImage.setVisibility(View.GONE);
                this.rltEmoji.setVisibility(View.VISIBLE);
                return;
            } else {
                this.imgaddEmoji.setVisibility(View.GONE);
                this.f64ic = getCurrentInputConnection();
                char charAt = view.getTag().toString().charAt(0);
                if (Character.isLetter(charAt) && this.caps) {
                    charAt = Character.toUpperCase(charAt);
                }
                this.f64ic.commitText(String.valueOf(charAt), 1);
                this.text = (String) this.f64ic.getTextBeforeCursor(99999, 0);
                return;
            }
            this.f64ic.deleteSurroundingText(1, 0);
        }
    }

    private void setClick(View view) {
        for (int i = 0; i < this.listTab.size(); i++) {
            if (view == this.listTab.get(i)) {
                view.setBackgroundColor(Color.parseColor("#af000000"));
            } else {
                this.listTab.get(i).setBackgroundColor(Color.parseColor("#af000000"));
            }
        }
    }

    @Override
    public void doSomeThing(View view) {
        if (this.sharedPreferences.getBoolean(Constants.ENABLE_SOUND, false)) {
            playKeyboard(-13);
        }
        getCurrentInputConnection().commitText(((CustomAKeyBoard) view).getNumber(), 1);
    }

    private void commitTyped(InputConnection inputConnection) {
        InputConnection currentInputConnection = getCurrentInputConnection();
        if (this.mComposing.length() > 0) {
            currentInputConnection.commitText(this.mComposing, this.mComposing.length());
            this.mComposing.setLength(0);
            updateCandidates();
        }
    }

    private void updateShiftKeyState(EditorInfo editorInfo) {
        EditorInfo currentInputEditorInfo;
        if (editorInfo != null && this.keyboardView != null && this.mQwertyKeyboard == this.keyboardView.getKeyboard() && (currentInputEditorInfo = getCurrentInputEditorInfo()) != null && currentInputEditorInfo.inputType != 0) {
            getCurrentInputConnection().getCursorCapsMode(editorInfo.inputType);
        }
    }

    public void exeDelete() {
        if (this.sharedPreferences.getBoolean(Constants.ENABLE_SOUND, false)) {
            playKeyboard(-5);
        }
        InputConnection currentInputConnection = getCurrentInputConnection();
        this.text = (String) currentInputConnection.getTextBeforeCursor(99999, 0);
        if (((String) currentInputConnection.getSelectedText(0)) != null) {
            currentInputConnection.setComposingText("", 1);
            return;
        }
        if (this.text.length() > 0) {
            try {
                if (URLEncoder.encode(this.text.substring(this.text.length() - 1), "UTF-8").equals("%3F")) {
                    currentInputConnection.deleteSurroundingText(2, 0);
                } else {
                    currentInputConnection.deleteSurroundingText(1, 0);
                }
                return;
            } catch (UnsupportedEncodingException unused) {
                currentInputConnection.deleteSurroundingText(1, 0);
            }
        } else {
            currentInputConnection.deleteSurroundingText(1, 0);
            return;
        }
        currentInputConnection.deleteSurroundingText(1, 0);
    }

    @Override
    public void onUpdateSelection(int i, int i2, int i3, int i4, int i5, int i6) {
        super.onUpdateSelection(i, i2, i3, i4, i5, i6);
        if (this.mComposing.length() > 0) {
            if (i3 != i6 || i4 != i6) {
                this.mComposing.setLength(0);
                updateCandidates();
                InputConnection currentInputConnection = getCurrentInputConnection();
                if (currentInputConnection != null) {
                    currentInputConnection.finishComposingText();
                }
            }
        }
    }

    private void handleBackspace() {
        int length = this.mComposing.length();
        if (length > 1) {
            this.mComposing.delete(length - 1, length);
            getCurrentInputConnection().setComposingText(this.mComposing, 1);
            updateCandidates();
        } else if (length > 0) {
            this.mComposing.setLength(0);
            getCurrentInputConnection().commitText("", 0);
            updateCandidates();
        } else {
            keyDownUp(67);
        }
        updateShiftKeyState(getCurrentInputEditorInfo());
    }

    @Override
    public void onGetSuggestions(SuggestionsInfo[] suggestionsInfoArr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < suggestionsInfoArr.length; i++) {
            int suggestionsCount = suggestionsInfoArr[i].getSuggestionsCount();
            sb.append('\n');
            for (int i2 = 0; i2 < suggestionsCount; i2++) {
                sb.append("," + suggestionsInfoArr[i].getSuggestionAt(i2));
            }
            sb.append(" (" + suggestionsCount + ")");
        }
    }

    private void dumpSuggestionsInfoInternal(List<String> list, SuggestionsInfo suggestionsInfo, int i, int i2) {
        int suggestionsCount = suggestionsInfo.getSuggestionsCount();
        for (int i3 = 0; i3 < suggestionsCount; i3++) {
            list.add(suggestionsInfo.getSuggestionAt(i3));
        }
    }

    @Override
    @TargetApi(16)
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] sentenceSuggestionsInfoArr) {
        ArrayList arrayList = new ArrayList();
        for (SentenceSuggestionsInfo sentenceSuggestionsInfo : sentenceSuggestionsInfoArr) {
            for (int i = 0; i < sentenceSuggestionsInfo.getSuggestionsCount(); i++) {
                dumpSuggestionsInfoInternal(arrayList, sentenceSuggestionsInfo.getSuggestionsInfoAt(i), sentenceSuggestionsInfo.getOffsetAt(i), sentenceSuggestionsInfo.getLengthAt(i));
            }
        }
        setSuggestions(arrayList, true, true);
    }

    @RequiresApi(api = 16)
    private void updateCandidates() {
        if (this.mComposing.length() > 0) {
            ArrayList arrayList = new ArrayList();
            if (this.mScs != null) {
                this.mScs.getSentenceSuggestions(new TextInfo[]{new TextInfo(this.mComposing.toString())}, 9);
                setSuggestions(arrayList, true, true);
                return;
            }
            return;
        }
        setSuggestions(null, false, false);
    }

    public void setSuggestions(List<String> list, boolean z, boolean z2) {
        if (list != null && list.size() > 0) {
            setCandidatesViewShown(true);
        } else if (isExtractViewShown()) {
            setCandidatesViewShown(true);
        }
        this.mSuggestions = list;
        if (this.mCandidateView != null) {
            this.mCandidateView.setSuggestions(list, z, z2);
        }
    }

    @RequiresApi(api = 16)
    public void pickDefaultCandidate() {
        pickSuggestionManually(0);
    }

    @RequiresApi(api = 16)
    public void pickSuggestionManually(int i) {
        if (this.mCompletions != null && i >= 0 && i < this.mCompletions.length) {
            getCurrentInputConnection().commitCompletion(this.mCompletions[i]);
            updateShiftKeyState(getCurrentInputEditorInfo());
        } else if (this.mComposing.length() > 0) {
            if (this.mSuggestions != null && i >= 0) {
                this.mComposing.replace(0, this.mComposing.length(), this.mSuggestions.get(i));
            }
            commitTyped(getCurrentInputConnection());
        }
    }

    public void SpaceClicked(View view) {
        exeSpace();
    }

    public void exeSpace() {
        if (this.sharedPreferences.getBoolean(Constants.ENABLE_SOUND, false)) {
            playKeyboard(32);
        }
        getCurrentInputConnection().commitText(" ", 1);
    }


    public void setThemeForKeyboard() {
        String str = ShareTheme.getmIntance(this).getObjectTheme().getBackgroundKeyboard().getmStyleKeyboard();
        Log.d(TAG, "setThemeForKeyboard: style " + str);
        if (ShareTheme.getmIntance(this).getObjectTheme().getBackgroundKeyboard().isBackground()) {

            String imagePath = ShareTheme.getmIntance(this).getObjectTheme().getBackgroundKeyboard().getmPath_bg();
            Log.d(TAG, "getBackgroundKeyboard: " + imagePath);

            DrawableFromPathTask loadTask = new DrawableFromPathTask(new DrawableFromPathTask.OnDrawableLoadListener() {
                @Override
                public void onDrawableLoaded(Drawable drawable) {
                    Log.d(TAG, "done");
                    if (Build.VERSION.SDK_INT >= 16) {
                        try {
                            ServiceIKeyboard.this.lnlKeyboardGenneral.setBackground(drawable);
                            ServiceIKeyboard.this.keyboardView.setBackground(null);
                            Log.d(TAG, "setBackground: " + drawable);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ServiceIKeyboard.this.lnlKeyboardGenneral.setBackgroundDrawable(drawable);
                        if (Build.VERSION.SDK_INT >= 16) {
                            ServiceIKeyboard.this.keyboardView.setBackground(null);
                        }
                    }
                }

                @Override
                public void onDrawableLoadError(String errorMessage) {
                    Log.e("DrawableLoadError", errorMessage);
                }
            });

            loadTask.execute(imagePath);
        } else if (ShareTheme.getmIntance(this).getObjectTheme().getBackgroundKeyboard().isAssets()) {
            try {
                this.f63d = Drawable.createFromStream(getAssets().open(ShareTheme.getmIntance(this).getObjectTheme().getBackgroundKeyboard().getmPath_bg()), null);
                if (Build.VERSION.SDK_INT >= 16) {
                    this.lnlKeyboardGenneral.setBackground(this.f63d);
                    this.keyboardView.setBackground(null);
                } else {
                    this.lnlKeyboardGenneral.setBackgroundDrawable(this.f63d);
                    if (Build.VERSION.SDK_INT >= 16) {
                        this.keyboardView.setBackground(null);
                    }
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } else if (ShareTheme.getmIntance(this).getObjectTheme().getBackgroundKeyboard().isDrawable()) {
            try {
                this.f63d = Drawable.createFromStream(getAssets().open(ShareTheme.getmIntance(this).getObjectTheme().getBackgroundKeyboard().getmPath_bg()), null);

                if (Build.VERSION.SDK_INT >= 16) {
                    this.lnlKeyboardGenneral.setBackground(this.f63d);
                    this.keyboardView.setBackground(null);
                } else {
                    this.lnlKeyboardGenneral.setBackgroundDrawable(this.f63d);
                    if (Build.VERSION.SDK_INT >= 16) {
                        this.keyboardView.setBackground(null);
                    }
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } else {//Color
            int parseColor = Color.parseColor(ShareTheme.getmIntance(this).getObjectTheme().getBackgroundKeyboard().getmColor_bg());
            this.lnlKeyboardGenneral.setBackgroundColor(parseColor);
            this.keyboardView.setBackgroundColor(parseColor);
        }
        if (str.equalsIgnoreCase(Constants.THEME_7)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme15_shift, R.drawable.theme15_shift, R.drawable.theme15_key, R.drawable.theme15_key, R.drawable.theme15_shift, R.drawable.theme15_key, R.drawable.theme15_key, R.drawable.theme15_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme15_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_white, R.mipmap.ic_delete_white1, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme15_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_8)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme16_shift, R.drawable.theme16_shift, R.drawable.theme16_key, R.drawable.theme16_key, R.drawable.theme16_shift, R.drawable.theme16_key, R.drawable.theme16_key, R.drawable.theme16_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme16_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_white, R.mipmap.ic_delete_white1, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme16_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_9)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme17_shift, R.drawable.theme17_shift, R.drawable.theme17_key, R.drawable.theme17_key, R.drawable.theme17_shift, R.drawable.theme17_key, R.drawable.theme17_key, R.drawable.theme17_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme17_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_white, R.mipmap.ic_delete_white1, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme17_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_10)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme18_shift, R.drawable.theme18_shift, R.drawable.theme18_key, R.drawable.theme18_key, R.drawable.theme18_shift, R.drawable.theme18_key, R.drawable.theme18_key, R.drawable.theme18_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme18_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_white, R.mipmap.ic_delete_white1, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme18_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_11)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme19_shift, R.drawable.theme19_shift, R.drawable.theme19_key, R.drawable.theme19_key, R.drawable.theme19_shift, R.drawable.theme19_key, R.drawable.theme19_key, R.drawable.theme19_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme19_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_white, R.mipmap.ic_delete_white1, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme19_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_12)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme20_shift, R.drawable.theme20_shift, R.drawable.theme20_key, R.drawable.theme20_key, R.drawable.theme20_abc, R.drawable.theme20_space, R.drawable.theme20_key, R.drawable.theme20_enter);
            this.keyboardView.setBackgroundKey(R.drawable.theme20_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
            this.keyboardView.setBackgroundSpace(R.drawable.theme20_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(Color.parseColor("#548800"), Color.parseColor("#548800"));
        } else if (str.equalsIgnoreCase(Constants.THEME_13)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme21_shift, R.drawable.theme21_shift, R.drawable.theme21_key, R.drawable.theme21_key, R.drawable.theme21_shift, R.drawable.theme21_key, R.drawable.theme21_key, R.drawable.theme21_enter);
            this.keyboardView.setBackgroundKey(R.drawable.theme21_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
            this.keyboardView.setBackgroundSpace(R.drawable.theme21_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(Color.parseColor("#E95081"), Color.parseColor("#E95081"));
        } else if (str.equalsIgnoreCase(Constants.THEME_14)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme22_shift, R.drawable.theme22_shift, R.drawable.theme22_key, R.drawable.theme22_key, R.drawable.theme22_shift, R.drawable.theme22_key, R.drawable.theme22_key, R.drawable.theme22_enter);
            this.keyboardView.setBackgroundKey(R.drawable.theme22_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
            this.keyboardView.setBackgroundSpace(R.drawable.theme22_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(Color.parseColor("#E95081"), Color.parseColor("#E95081"));
        } else if (str.equalsIgnoreCase(Constants.THEME_15)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme1_shift, R.drawable.theme1_shift, R.drawable.color1_key, R.drawable.theme1_mic, R.drawable.theme1_abc, R.drawable.color1_key, R.drawable.theme1_mic, R.drawable.theme1_enter);
            this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
            this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
            this.keyboardView.setColorTextSpace(-1);
            this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
        } else if (str.equalsIgnoreCase(Constants.THEME_16)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme1_shift, R.drawable.theme1_shift, R.drawable.theme2_key, R.drawable.theme1_mic, R.drawable.theme1_abc, R.drawable.theme2_key, R.drawable.theme1_mic, R.drawable.theme1_enter);
            this.keyboardView.setBackgroundKey(R.drawable.theme2_key, R.drawable.button_click2);
            this.keyboardView.setBackgroundSpace(R.drawable.theme2_space, R.drawable.bottonspace_click2);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
            this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
        } else if (str.equalsIgnoreCase(Constants.THEME_17)) {
            this.keyboardView.setBackgroundButton(R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key, R.drawable.theme3_enter);
            this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click3);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
            this.keyboardView.setBackgroundSpace(R.drawable.theme2_space, R.drawable.bottonspace_click3);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
        } else if (str.equalsIgnoreCase(Constants.THEME_18)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme4_key, R.drawable.theme4_key, R.drawable.theme4_key, R.drawable.theme4_key, R.drawable.theme4_key, R.drawable.theme4_key, R.drawable.theme4_key, R.drawable.theme4_key);
            this.keyboardView.setBackgroundKey(R.drawable.theme4_key, R.drawable.button_click4);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_white, R.mipmap.ic_delete_white1, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme4_space, R.drawable.bottonspace_click4);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_19)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme5_key, R.drawable.theme5_key, R.drawable.theme5_key, R.drawable.theme5_key, R.drawable.theme5_key, R.drawable.theme5_key, R.drawable.theme5_key, R.drawable.theme3_enter);
            this.keyboardView.setBackgroundKey(R.drawable.theme5_key, R.drawable.button_click5);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
            this.keyboardView.setBackgroundSpace(R.drawable.theme5_space, R.drawable.bottonspace_click5);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
        } else if (str.equalsIgnoreCase(Constants.THEME_20)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme6_shift, R.drawable.theme6_shift, R.drawable.theme6_key, R.drawable.theme6_key, R.drawable.theme6_shift, R.drawable.theme6_key, R.drawable.theme6_key, R.drawable.theme6_enter);
            this.keyboardView.setBackgroundKey(R.drawable.theme6_key, R.drawable.button_click6);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_white, R.mipmap.ic_delete_white1, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme6_space, R.drawable.bottonspace_click6);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_21)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme7_shift, R.drawable.theme7_shift, R.drawable.theme7_key, R.drawable.theme7_key, R.drawable.theme7_shift, R.drawable.theme7_key, R.drawable.theme7_key, R.drawable.theme7_enter);
            this.keyboardView.setBackgroundKey(R.drawable.theme7_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_white, R.mipmap.ic_delete_white1, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme7_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_22)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme8_shift, R.drawable.theme8_shift, R.drawable.theme8_key, R.drawable.theme8_mic, R.drawable.theme8_shift, R.drawable.theme8_key, R.drawable.theme8_mic, R.drawable.theme8_enter);
            this.keyboardView.setBackgroundKey(R.drawable.theme8_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
            this.keyboardView.setBackgroundSpace(R.drawable.theme8_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
        } else if (str.equalsIgnoreCase(Constants.THEME_23)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme23_shift, R.drawable.theme23_delete, R.drawable.theme23_icon, R.drawable.theme23_dot, R.drawable.theme23_123, R.drawable.theme23_space, R.drawable.theme23_dot, R.drawable.theme23_enter);
            this.keyboardView.setBackgroundKey(R.drawable.theme23_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white_new, R.mipmap.img_dot_white, R.mipmap.ic_deee, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme23_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_24)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme24_shift, R.drawable.theme24_delete, R.drawable.theme24_dot, R.drawable.theme24_dot, R.drawable.theme24_123, R.drawable.theme24_space, R.drawable.theme24_dot, R.drawable.theme24_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme24_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white_new, R.mipmap.img_dot_white, R.mipmap.ic_deee, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme24_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(Color.parseColor("#FFBC37"), Color.parseColor("#FFBC37"));
        } else if (str.equalsIgnoreCase(Constants.THEME_25)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme25_shift, R.drawable.theme25_delete, R.drawable.theme25_key, R.drawable.theme25_key, R.drawable.theme25_123, R.drawable.theme25_key, R.drawable.theme25_key, R.drawable.theme25_enter);
            this.keyboardView.setBackgroundKey(R.drawable.theme25_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white_new, R.mipmap.img_dot_white, R.mipmap.ic_deee, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme25_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_26)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme26_delete, R.drawable.theme26_delete, R.drawable.theme26_key, R.drawable.theme26_key, R.drawable.theme26_shift, R.drawable.theme26_key, R.drawable.theme26_key, R.drawable.theme26_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme26_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white_new, R.mipmap.img_dot_white, R.mipmap.ic_deee, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme26_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
        } else if (str.equalsIgnoreCase(Constants.THEME_27)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme27_shift, R.drawable.theme27_shift, R.drawable.theme27_key, R.drawable.theme27_key, R.drawable.theme27_shift, R.drawable.theme27_key, R.drawable.theme27_key, R.drawable.theme27_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme27_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white_new, R.mipmap.img_dot_white, R.mipmap.ic_deee, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme27_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
        } else if (str.equalsIgnoreCase(Constants.THEME_28)) {
            //(shift,delete,key_sample,key_sample,123,key_sample,enter)
            this.keyboardView.setBackgroundButton(R.drawable.theme28_shift, R.drawable.theme28_shift, R.drawable.theme28_key, R.drawable.theme28_key, R.drawable.theme28_shift, R.drawable.theme28_key, R.drawable.theme28_key, R.drawable.theme28_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme28_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.shift_organe, R.mipmap.dot_orange, R.mipmap.delete_organ, R.mipmap.emoij_orange, R.mipmap.switch_keyboard_orange, R.mipmap.enter_orange);
            this.keyboardView.setBackgroundSpace(R.drawable.theme28_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(Color.parseColor("#D94E00"), Color.parseColor("#D94E00"));
        } else if (str.equalsIgnoreCase(Constants.THEME_29)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme29_shift, R.drawable.theme29_shift, R.drawable.theme29_key, R.drawable.theme29_key, R.drawable.theme29_shift, R.drawable.theme29_key, R.drawable.theme29_key, R.drawable.theme29_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme29_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white_new, R.mipmap.img_dot_white, R.mipmap.ic_deee, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme29_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_30)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme30_shift, R.drawable.theme30_shift, R.drawable.theme30_key, R.drawable.theme30_key, R.drawable.theme30_shift, R.drawable.theme30_key, R.drawable.theme30_key, R.drawable.theme30_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme30_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white_new, R.mipmap.img_dot_white, R.mipmap.ic_deee, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme30_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_31)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme31_shift, R.drawable.theme31_shift, R.drawable.theme31_key, R.drawable.theme31_key, R.drawable.theme31_shift, R.drawable.theme31_key, R.drawable.theme31_key, R.drawable.theme31_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme31_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white_new, R.mipmap.img_dot_white, R.mipmap.ic_deee, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme31_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_32)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key);
            this.keyboardView.setBackgroundKey(R.drawable.theme23_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white_new, R.mipmap.img_dot_white, R.mipmap.ic_deee, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.key_space_trans, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_33)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key);
            this.keyboardView.setBackgroundKey(R.drawable.theme23_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white_new, R.mipmap.img_dot_white, R.mipmap.ic_deee, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.key_space_trans, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_34)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key);
            this.keyboardView.setBackgroundKey(R.drawable.theme23_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white_new, R.mipmap.img_dot_white, R.mipmap.ic_deee, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.key_space_trans, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_35)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key, R.drawable.theme23_key);
            this.keyboardView.setBackgroundKey(R.drawable.theme23_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white_new, R.mipmap.img_dot_white, R.mipmap.ic_deee, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.key_space_trans, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_36)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme_36_shift, R.drawable.theme36_delete, R.drawable.theme36_emoji, R.drawable.theme_36_shift, R.drawable.theme_36_shift, R.drawable.theme_36_shift, R.drawable.theme36_dot, R.drawable.theme36_enter);
            this.keyboardView.setBackgroundKey(R.drawable.theme36_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white_new, R.mipmap.img_dot_white, R.mipmap.ic_deee, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme36_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(Color.parseColor("#FF3D33"), Color.parseColor("#FF3D33"));
        } else if (str.equalsIgnoreCase(Constants.THEME_37)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme_37_key, R.drawable.theme_37_key, R.drawable.theme_37_key, R.drawable.theme_37_key, R.drawable.theme_37_key, R.drawable.theme_37_key, R.drawable.theme_37_key, R.drawable.theme_37_key);
            this.keyboardView.setBackgroundKey(R.drawable.theme_37_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white_new, R.mipmap.img_dot_white, R.mipmap.ic_deee, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme37_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_38)) {
            // update 38
            this.keyboardView.setBackgroundButton(
                    R.drawable.theme_38_shift, // shift
                    R.drawable.theme_38_delete, // delete
                    R.drawable.theme_38_emoji, // emoji
                    R.drawable.theme_38_micro, // micro
                    R.drawable.theme_38_abc, // ABC
                    R.drawable.theme38_space, // space
                    R.drawable.theme_38_dot, // dot
                    R.drawable.theme_38_enter // enter
            );

            this.keyboardView.setBackgroundKey(
                    R.drawable.theme_38_key,
                    R.drawable.button_click7
            );

            this.keyboardView.setImageButton(
                    R.mipmap.theme_38_key, // imgShift
                    R.mipmap.theme_38_key, // imgDot
                    R.mipmap.theme_38_key, // imgDelete
                    R.mipmap.theme_38_key, // imgEmoji
                    R.mipmap.theme_38_key, // imgMicro
                    R.mipmap.theme_38_key // imgEnter
            );
            this.keyboardView.setBackgroundSpace(
                    R.drawable.theme38_space,
                    R.drawable.bottonspace_click7
            );
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
            // update 38
        } else if (str.equalsIgnoreCase(Constants.THEME_39)) {
            // update 39
            this.keyboardView.setBackgroundButton(
                    R.drawable.theme_39_shift, // shift
                    R.drawable.theme_39_delete, // delete
                    R.drawable.theme_39_emoji, // emoji
                    R.drawable.theme_39_micro, // micro
                    R.drawable.theme_39_abc, // ABC
                    R.drawable.theme_39_space, // space
                    R.drawable.theme_39_dot, // dot
                    R.drawable.theme_39_enter // enter
            );

            this.keyboardView.setBackgroundKey(
                    R.drawable.theme_39_key,
                    R.drawable.button_click7
            );

            this.keyboardView.setImageButton(
                    R.mipmap.theme_38_key, // imgShift
                    R.mipmap.theme_38_key, // imgDot
                    R.mipmap.theme_38_key, // imgDelete
                    R.mipmap.theme_38_key, // imgEmoji
                    R.mipmap.theme_38_key, // imgMicro
                    R.mipmap.theme_38_key // imgEnter
            );
            this.keyboardView.setBackgroundSpace(
                    R.drawable.theme_39_space,
                    R.drawable.bottonspace_click7
            );
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(Color.parseColor("#FF6533"), Color.parseColor("#FF6533"));
            // update 39
        } else if (str.equalsIgnoreCase(Constants.THEME_40)) {
            // update 40
            this.keyboardView.setBackgroundButton(
                    R.drawable.theme_40_shift, // shift
                    R.drawable.theme_40_delete, // delete
                    R.drawable.theme_40_emoj, // emoji
                    R.drawable.theme_40_micro, // micro
                    R.drawable.theme_40_key, // ABC
                    R.drawable.theme40_space, // space
                    R.drawable.theme_40_dot, // dot
                    R.drawable.theme_40_enter // enter
            );

            this.keyboardView.setBackgroundKey(
                    R.drawable.theme_40_key,
                    R.drawable.button_click7
            );

            this.keyboardView.setImageButton(
                    R.mipmap.theme_38_key, // imgShift
                    R.mipmap.theme_38_key, // imgDot
                    R.mipmap.theme_38_key, // imgDelete
                    R.mipmap.theme_38_key, // imgEmoji
                    R.mipmap.theme_38_key, // imgMicro
                    R.mipmap.theme_38_key // imgEnter
            );
            this.keyboardView.setBackgroundSpace(
                    R.drawable.theme40_space,
                    R.drawable.bottonspace_click7
            );
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
            // update 40
        } else if (str.equalsIgnoreCase(Constants.THEME_41)) {
            // update 41
            this.keyboardView.setBackgroundButton(
                    R.drawable.theme_41_shift, // shift
                    R.drawable.theme_41_delete, // delete
                    R.drawable.theme_41_emoj, // emoji
                    R.drawable.theme_41_micro, // micro
                    R.drawable.theme_41_key, // ABC
                    R.drawable.theme41_space, // space
                    R.drawable.theme_41_dot, // dot
                    R.drawable.theme_41_enter // enter
            );

            this.keyboardView.setBackgroundKey(
                    R.drawable.theme_41_key,
                    R.drawable.button_click7
            );

            this.keyboardView.setImageButton(
                    R.mipmap.theme_38_key, // imgShift
                    R.mipmap.theme_38_key, // imgDot
                    R.mipmap.theme_38_key, // imgDelete
                    R.mipmap.theme_38_key, // imgEmoji
                    R.mipmap.theme_38_key, // imgMicro
                    R.mipmap.theme_38_key // imgEnter
            );
            this.keyboardView.setBackgroundSpace(
                    R.drawable.theme41_space,
                    R.drawable.bottonspace_click7
            );
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(Color.parseColor("#FF6533"), Color.parseColor("#FF6533"));
            // update 41
        } else if (str.equalsIgnoreCase(Constants.THEME_42)) {
            // update 42
            this.keyboardView.setBackgroundButton(
                    R.drawable.theme_42_shift, // shift
                    R.drawable.theme_42_delete, // delete
                    R.drawable.theme_42_emoj, // emoji
                    R.drawable.theme_42_micro, // micro
                    R.drawable.theme_42_abc, // ABC
                    R.drawable.theme42_space, // space
                    R.drawable.theme_42_dot, // dot
                    R.drawable.theme_42_enter // enter
            );

            this.keyboardView.setBackgroundKey(
                    R.drawable.theme_42_key,
                    R.drawable.button_click7
            );

            this.keyboardView.setImageButton(
                    R.mipmap.theme_38_key, // imgShift
                    R.mipmap.theme_38_key, // imgDot
                    R.mipmap.theme_38_key, // imgDelete
                    R.mipmap.theme_38_key, // imgEmoji
                    R.mipmap.theme_38_key, // imgMicro
                    R.mipmap.theme_38_key // imgEnter
            );
            this.keyboardView.setBackgroundSpace(
                    R.drawable.theme42_space,
                    R.drawable.bottonspace_click7
            );
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
            // update 42
        } else if (str.equalsIgnoreCase(Constants.THEME_43)) {
            // update 43
            this.keyboardView.setBackgroundButton(
                    R.drawable.theme_43_shift, // shift
                    R.drawable.theme_43_delete, // delete
                    R.drawable.theme_43_emoj, // emoji
                    R.drawable.theme_43_micro, // micro
                    R.drawable.theme_43_abc, // ABC
                    R.drawable.theme43_space, // space
                    R.drawable.theme_43_dot, // dot
                    R.drawable.theme_43_enter // enter
            );

            this.keyboardView.setBackgroundKey(
                    R.drawable.theme_43_key,
                    R.drawable.button_click7
            );

            this.keyboardView.setImageButton(
                    R.mipmap.theme_38_key, // imgShift
                    R.mipmap.theme_38_key, // imgDot
                    R.mipmap.theme_38_key, // imgDelete
                    R.mipmap.theme_38_key, // imgEmoji
                    R.mipmap.theme_38_key, // imgMicro
                    R.mipmap.theme_38_key // imgEnter
            );
            this.keyboardView.setBackgroundSpace(
                    R.drawable.theme43_space,
                    R.drawable.bottonspace_click7
            );
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
            // update 43
        } else if (str.equalsIgnoreCase(Constants.THEME_44)) {
            // update 44
            this.keyboardView.setBackgroundButton(
                    R.drawable.ic_shift_44_theme, // shift
                    R.drawable.ic_delete_44, // delete
                    R.drawable.ic_emoj_44_1, // emoji
                    R.drawable.ic_lang_theme_43, // micro
                    R.drawable.ic_abc_44_1, // ABC
                    R.drawable.space_44, // space
                    R.drawable.ic_dot_44_1, // dot
                    R.drawable.ic_enter_44_1 // enter
            );

            this.keyboardView.setBackgroundKey(
                    R.mipmap.theme_38_key,
                    R.drawable.button_click7
            );

            this.keyboardView.setImageButton(
                    R.mipmap.theme_38_key, // imgShift
                    R.mipmap.theme_38_key, // imgDot
                    R.mipmap.theme_38_key, // imgDelete
                    R.mipmap.theme_38_key, // imgEmoji
                    R.mipmap.theme_38_key, // imgMicro
                    R.mipmap.theme_38_key // imgEnter
            );
            this.keyboardView.setBackgroundSpace(
                    R.drawable.space_44,
                    R.drawable.bottonspace_click7
            );
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(Color.parseColor("#16FFF1"), Color.parseColor("#16FFF1"));
            // update 44
        } else if (str.equalsIgnoreCase(Constants.THEME_45)) {
            // update 45
            this.keyboardView.setBackgroundButton(
                    R.drawable.ic_shift_45, // shift
                    R.drawable.ic_delete_45, // delete
                    R.drawable.ic_emoji_45, // emoji
                    R.drawable.ic_lang_45, // micro
                    R.drawable.ic_abc_45, // ABC
                    R.drawable.ic_space_45_1, // space
                    R.drawable.ic_dot_45, // dot
                    R.drawable.ic_enter_45 // enter
            );

            this.keyboardView.setBackgroundKey(
                    R.mipmap.theme_38_key,
                    R.drawable.button_click7
            );

            this.keyboardView.setImageButton(
                    R.mipmap.theme_38_key, // imgShift
                    R.mipmap.theme_38_key, // imgDot
                    R.mipmap.theme_38_key, // imgDelete
                    R.mipmap.theme_38_key, // imgEmoji
                    R.mipmap.theme_38_key, // imgMicro
                    R.mipmap.theme_38_key // imgEnter
            );
            this.keyboardView.setBackgroundSpace(
                    R.drawable.ic_space_45_1,
                    R.drawable.bottonspace_click7
            );
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(Color.parseColor("#FEFFC9"), Color.parseColor("#FEFFC9"));
            // update 45
        } else if (str.equalsIgnoreCase(Constants.THEME_1)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme9_shift, R.drawable.theme9_shift, R.drawable.theme9_key, R.drawable.theme9_key, R.drawable.theme9_shift, R.drawable.theme9_key, R.drawable.theme9_key, R.drawable.theme9_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme9_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_white, R.mipmap.ic_deee, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme9_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_2)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme10_shift, R.drawable.theme10_shift, R.drawable.theme10_key, R.drawable.theme10_key, R.drawable.theme10_shift, R.drawable.theme10_key, R.drawable.theme10_key, R.drawable.theme10_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme10_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_white, R.mipmap.ic_deee, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme10_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_3)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme11_shift, R.drawable.theme11_shift, R.drawable.theme11_key, R.drawable.theme11_key, R.drawable.theme11_shift, R.drawable.theme11_key, R.drawable.theme11_key, R.drawable.theme11_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme11_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_white, R.mipmap.ic_delete_white1, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme11_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_4)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme12_shift, R.drawable.theme12_shift, R.drawable.theme12_key, R.drawable.theme12_key, R.drawable.theme12_shift, R.drawable.theme12_key, R.drawable.theme12_key, R.drawable.theme12_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme12_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_white, R.mipmap.ic_delete_white1, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme12_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_5)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme13_shift, R.drawable.theme13_shift, R.drawable.theme13_key, R.drawable.theme13_key, R.drawable.theme13_enter, R.drawable.theme13_key, R.drawable.theme13_key, R.drawable.theme13_enter);
            this.keyboardView.setBackgroundKey(R.drawable.theme13_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_black, R.mipmap.ic_delete_white1, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_6)) {
            this.keyboardView.setBackgroundButton(R.drawable.theme14_shift, R.drawable.theme14_shift, R.drawable.theme14_key, R.drawable.theme14_key, R.drawable.theme14_shift, R.drawable.theme14_key, R.drawable.theme14_key, R.drawable.theme14_shift);
            this.keyboardView.setBackgroundKey(R.drawable.theme14_key, R.drawable.button_click7);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_white, R.mipmap.ic_delete_white1, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme14_space, R.drawable.bottonspace_click7);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
        } else if (str.equalsIgnoreCase(Constants.THEME_IOS_DEFAULT)) {
            try {
                this.keyboardView.setBackgroundButton(R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key);
                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.ic_delete_white1, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        } else if (str.equalsIgnoreCase(Constants.THEME_GREEN_MEDIUM)) {
            this.keyboardView.setBackgroundButton(R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_key);
            this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.ic_delete_white1, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
        }

        //COLOR
        SharedPreferences sharedPreferences = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE);
        int color = Integer.parseInt(sharedPreferences.getString("color", "30121999"));

        if (color == getResources().getColor(R.color.color_light_pink)) {
            this.keyboardView.setBackgroundButton(R.drawable.color1_shift, R.drawable.color1_shift, R.drawable.color1_dot, R.drawable.color1_dot, R.drawable.color1_123, R.drawable.color1_key, R.drawable.color1_dot, R.drawable.color1_123);
            this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
            this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
        } else if (color == getResources().getColor(R.color.color_Ivory)) {
            this.keyboardView.setBackgroundButton(R.drawable.color2_shift, R.drawable.color2_shift, R.drawable.color2_dot, R.drawable.color2_dot, R.drawable.color2_123, R.drawable.color2_key, R.drawable.color2_dot, R.drawable.color2_123);
            this.keyboardView.setBackgroundKey(R.drawable.color2_key, R.drawable.button_click1);
            this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_white, R.mipmap.ic_delete_white1, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
            this.keyboardView.setBackgroundSpace(R.drawable.color2_space, R.drawable.bottonspace_click1);
            this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
            this.keyboardView.colorText(-1, -1);
//        switch (Integer.parseInt(sharedPreferences.getString("color", "30121999"))) {
//            case R.color.color_light_pink:
//                this.keyboardView.setBackgroundButton(R.drawable.color1_shift, R.drawable.color1_shift, R.drawable.color1_dot, R.drawable.color1_dot, R.drawable.color1_123, R.drawable.color1_key, R.drawable.color1_dot, R.drawable.color1_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Ivory:
//                this.keyboardView.setBackgroundButton(R.drawable.color2_shift, R.drawable.color2_shift, R.drawable.color2_dot, R.drawable.color2_dot, R.drawable.color2_123, R.drawable.color2_key, R.drawable.color2_dot, R.drawable.color2_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color2_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_white, R.mipmap.ic_delete_white1, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
//                this.keyboardView.setBackgroundSpace(R.drawable.color2_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(-1, -1);
//                break;
//            case R.color.white:
//                this.keyboardView.setBackgroundButton(R.drawable.color3_shift, R.drawable.color3_shift, R.drawable.color3_dot, R.drawable.color3_dot, R.drawable.color3_123, R.drawable.color3_key, R.drawable.color3_dot, R.drawable.color3_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color3_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.color3_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(-1, -1);
//                break;
//            case R.color.color_Peach:
//                this.keyboardView.setBackgroundButton(R.drawable.color4_shift, R.drawable.color4_shift, R.drawable.color4_dot, R.drawable.color4_dot, R.drawable.color4_123, R.drawable.color1_key, R.drawable.color4_dot, R.drawable.color4_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_white, R.mipmap.ic_delete_white1, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Royal_Blue:
//                this.keyboardView.setBackgroundButton(R.drawable.color5_shift, R.drawable.color5_shift, R.drawable.color1_key, R.drawable.color5_dot, R.drawable.color5_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color5_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_black, R.mipmap.ic_delete_white1, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Blue:
//                this.keyboardView.setBackgroundButton(R.drawable.color6_shift, R.drawable.color6_shift, R.drawable.color1_key, R.drawable.color6_dot, R.drawable.color6_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color6_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_black, R.mipmap.ic_delete_white1, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Purple:
//                this.keyboardView.setBackgroundButton(R.drawable.color7_shift, R.drawable.color7_shift, R.drawable.color1_key, R.drawable.color7_dot, R.drawable.color7_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color7_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_black, R.mipmap.ic_delete_white1, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Lavender:
//                this.keyboardView.setBackgroundButton(R.drawable.color8_shift, R.drawable.color8_shift, R.drawable.color1_key, R.drawable.color8_dot, R.drawable.color8_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color8_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Pacific_Blue:
//                this.keyboardView.setBackgroundButton(R.drawable.color9_shift, R.drawable.color9_shift, R.drawable.color1_key, R.drawable.color9_dot, R.drawable.color9_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color9_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_white, R.mipmap.img_dot_black, R.mipmap.ic_delete_white1, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_white, R.mipmap.enter_white);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Violet:
//                this.keyboardView.setBackgroundButton(R.drawable.color10_shift, R.drawable.color10_shift, R.drawable.color1_key, R.drawable.color10_dot, R.drawable.color10_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color10_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Blue_Gray:
//                this.keyboardView.setBackgroundButton(R.drawable.color11_shift, R.drawable.color11_shift, R.drawable.color1_key, R.drawable.color11_dot, R.drawable.color11_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color11_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Dark_Blue:
//                this.keyboardView.setBackgroundButton(R.drawable.color12_shift, R.drawable.color12_shift, R.drawable.color12_key, R.drawable.color12_dot, R.drawable.color12_123, R.drawable.color12_key, R.drawable.color12_key, R.drawable.color12_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color12_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_white, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.color12_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Pink:
//                this.keyboardView.setBackgroundButton(R.drawable.color13_shift, R.drawable.color13_shift, R.drawable.color1_key, R.drawable.color13_dot, R.drawable.color1_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Salmon_Pink:
//                this.keyboardView.setBackgroundButton(R.drawable.color14_shift, R.drawable.color14_shift, R.drawable.color1_key, R.drawable.color14_dot, R.drawable.color1_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Coral:
//                this.keyboardView.setBackgroundButton(R.drawable.color15_shift, R.drawable.color15_shift, R.drawable.color1_key, R.drawable.color15_dot, R.drawable.color1_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color1_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Vermilion:
//                this.keyboardView.setBackgroundButton(R.drawable.color16_shift, R.drawable.color16_shift, R.drawable.color1_key, R.drawable.color16_dot, R.drawable.color16_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color16_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Light_Green:
//                this.keyboardView.setBackgroundButton(R.drawable.color17_shift, R.drawable.color17_shift, R.drawable.color1_key, R.drawable.color17_dot, R.drawable.color17_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color17_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Teal:
//                this.keyboardView.setBackgroundButton(R.drawable.color18_shift, R.drawable.color18_shift, R.drawable.color1_key, R.drawable.color18_dot, R.drawable.color18_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color18_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Greenland:
//                this.keyboardView.setBackgroundButton(R.drawable.color19_shift, R.drawable.color19_shift, R.drawable.color1_key, R.drawable.color19_dot, R.drawable.color19_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color19_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Pale_Yellow:
//                this.keyboardView.setBackgroundButton(R.drawable.color20_shift, R.drawable.color20_shift, R.drawable.color1_key, R.drawable.color20_dot, R.drawable.color20_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color20_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Golden1:
//                this.keyboardView.setBackgroundButton(R.drawable.color21_shift, R.drawable.color21_shift, R.drawable.color1_key, R.drawable.color21_dot, R.drawable.color21_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color21_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Golden2:
//                this.keyboardView.setBackgroundButton(R.drawable.color22_shift, R.drawable.color22_shift, R.drawable.color1_key, R.drawable.color22_dot, R.drawable.color22_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color22_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Copper:
//                this.keyboardView.setBackgroundButton(R.drawable.color23_shift, R.drawable.color23_shift, R.drawable.color1_key, R.drawable.color23_dot, R.drawable.color23_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color23_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            case R.color.color_Orange_pink:
//                this.keyboardView.setBackgroundButton(R.drawable.color24_shift, R.drawable.color23_shift, R.drawable.color1_key, R.drawable.color23_dot, R.drawable.color23_123, R.drawable.color1_key, R.drawable.color1_key, R.drawable.color23_123);
//                this.keyboardView.setBackgroundKey(R.drawable.color1_key, R.drawable.button_click1);
//                this.keyboardView.setImageButton(R.mipmap.ic_shift_black, R.mipmap.img_dot_black, R.mipmap.delete_black, R.mipmap.emoji_black, R.mipmap.img_switch_keyboard_black, R.mipmap.enter_black);
//                this.keyboardView.setBackgroundSpace(R.drawable.theme1_space, R.drawable.bottonspace_click1);
//                this.keyboardView.setColorTextSpace(ViewCompat.MEASURED_STATE_MASK);
//                this.keyboardView.colorText(ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
//                break;
//            default:
//                break;
        }
    }

    public ArrayList<Bitmap> bitmapArrayList(Context context, String str) {
        ArrayList<Bitmap> arrayList = new ArrayList<>();
        try {
            String[] list = context.getAssets().list(str);
            for (String str2 : list) {
                arrayList.add(BitmapFactory.decodeStream(context.getAssets().open(str + File.separator + str2)));
            }
        } catch (Exception unused) {
        }
        return arrayList;
    }

    public void MicroPhoneClicked() {
        if (this.sharedPreferences.getBoolean(Constants.ENABLE_SOUND, false)) {
            playKeyboard(-5);
        }
        //callVoid();
        selectDefaultKeyboard();
    }

    private void selectDefaultKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showInputMethodPicker();
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    private IBinder getToken() {
        final Dialog dialog = getWindow();
        if (dialog == null) {
            return null;
        }
        final Window window = dialog.getWindow();
        if (window == null) {
            return null;
        }
        return window.getAttributes().token;
    }
}
