package com.example.kusitmsresult.Travel_Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kusitmsresult.R;
import com.example.kusitmsresult.domain.userData;
import com.example.kusitmsresult.domain.userTable;
import com.example.kusitmsresult.model.AboveModel;
import com.example.kusitmsresult.model.Distance;
import com.example.kusitmsresult.model.DistanceRadius;
import com.example.kusitmsresult.model.MarkerModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakue.lakuepopupactivity.PopupActivity;
import com.lakue.lakuepopupactivity.PopupResult;
import com.lakue.lakuepopupactivity.PopupType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class StartTravelActivity extends FragmentActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {
    //======================================================
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient = null;
    private Marker currentMarker = null;

    private static final String TAG = "googlemap_example";
    private static final long requestInterval = 10000; //1sec
    private static final long fastInterval = 10000 / 2; //0.5sec

//    private MapsActivity mActivity = this;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng latLng;
    private ArrayList<Double> latitude;
    private ArrayList<Double> longitude;
    private LocationRequest mLocationrequest;

    //private LocationSettingsRequest mLocationSetting;
    private LocationSettingsRequest.Builder builder;
    private SettingsClient settingClient;
    private Task<LocationSettingsResponse> task;
    private static final int request_validation = 50;
    private static final int removable_resolution = 100;
    private static boolean requestFlag;
//    boolean mMoveMapByUser = true;
//    boolean mMoveMapByAPI = true;

    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Location lastLocation; // lastLocation??? null????????? ???????????? ????????? ???????????? ????????? ??????
    private PolylineOptions polylineOption;
    private CameraUpdate zoom;
    private CameraUpdate center;
    private static int count = 1;
    private TextView distText;
    private FloatingActionButton start;
    private FloatingActionButton stop;
    private FloatingActionButton pause;
    private Bitmap startImage;
    private Bitmap stopImage;
    private Bitmap pauseImage;
    private Bitmap playImage;
    private float distance = 0;
    private static long totalTime = 0;
    private long time1 = 0;
    private TextView distanceView;
    private TextView timeView;
    private userTable user;
    private NavigationView navigationView;
    private Intent intent;
    private boolean activityStateIndicator = false;
    private boolean startButtonClickedState = false;
    private static Polyline polyline;
    private boolean statusClickToggle = false;
    private long heartRate = 0;
    private double calories = 0;
    private float minmumDistanceforRequest = 10;
    private UserService timeUpdate;
    private static double[] timeRecord = new double[4];
    private Thread timer;
    private boolean threadstate = false;
    private double speed = 0;
    private double speed2 = 0;
    private boolean speedstate = true;
    TextView textView;
    private String storename_selected;
    public String URLaddress; //??? ?????? ????????? ????????? URL
    public String get_storeName; //????????? ??????: ?????? ??????

    // ?????? ?????? ????????? ?????? ?????? ?????? ??????
    double current_location_x=1.0, current_location_y=1.0; // ??? ????????? ????????? ??????
    DistanceRadius pinning=new DistanceRadius(current_location_x, current_location_y);

    //?????? ??????????????? ?????????
    public ArrayList<MarkerModel> MarkerList = new ArrayList<>();

    public Integer onCount_markerList_index = 0;//


    //=========================================================== ??????
    public ArrayList<Marker> markerArrayList = new ArrayList<>();
    public ArrayList<MarkerOptions> markerOptionsArrayList = new ArrayList<>();
    private int REQUEST_CODE = 101; //storelistActivity ???????????? ?????????

    //===========================================================


    /* activity indentity*/
    private static final int startClickTriggered = 1;
    private static final String startRoute1 = "startRoute1";
    //  private static final String startRoute2 = "startRoute2";
    private static final int registerTriggered = 2;
    private static final int historyId = 3;
    private static final int statisticId = 4;


    //======================================================

    private ArrayList<String> startTravel_arrayList;
    private ArrayList<String> startTravel_timearrayList;
    private ArrayList<String> startTravel_datearrayList;



    private String resultArrayList = "";
    private String resulttimeArrayList = "";
    private String resultdateArrayList = "";


    private String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private DatabaseReference reference;


    public double[][] userRateList;

    private static String[] table_place;
    private static String[] table_user;
    private static double[][] array;
    private static double[][] filtered_array =   new double[7][7];

    private ArrayList<String> rankList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_travel);

        //============================================================

        Intent intent = getIntent();
        ArrayList<String>  userUidList = (ArrayList<String>) intent.getSerializableExtra("userUidList");
        ArrayList<String>  placeList = (ArrayList<String>) intent.getSerializableExtra("placeList");

        table_place = new String[placeList.size()];
        table_user = new String[userUidList.size()];
        userRateList = new double[placeList.size()][userUidList.size()];

        Random random = new Random();
        for(int i =0; i < placeList.size(); i++) {
            for(int j=0; j<userUidList.size(); j++) {
                userRateList[i][j] = random.nextInt(5) +1;
            }
        }



        for(int i = 0; i<placeList.size(); i++) {
            table_place[i] = placeList.get(i);
            Log.d("table", table_place[i]);
        }

        for(int j = 0; j<userUidList.size(); j++) {
            table_user[j] = userUidList.get(j);
            Log.d("table", table_user[j]);
        }



        array = userRateList;

        for (int i = 0; i < table_place.length; i++) {

            for (int j = i + 1; j < table_place.length; j++) {
                double square_sum1 = 0;
                double square_sum2 = 0;
                double dot_product = 0;
                double total = 0;

                for (int k = 0; k < table_user.length; k++) {
                    square_sum1 += Math.pow(array[i][k], 2);
                    square_sum2 += Math.pow(array[j][k], 2);
                    dot_product += array[i][k] * array[j][k];
                }
                square_sum1 = Math.sqrt(square_sum1);
                square_sum2 = Math.sqrt(square_sum2);
                total = dot_product / (square_sum1 * square_sum2);
                filtered_array[i][j] = Math.round(total * 1000) / 1000.0;
                filtered_array[j][i] = Math.round(total * 1000) / 1000.0;

            }
        }


        for(int i =0; i < placeList.size(); i++) {
            for(int j=0; j<userUidList.size(); j++) {
                System.out.print("THIS " + filtered_array[i][j]);
            }
            System.out.println();
        }

        double[][] filtered_array_copy = new double[filtered_array.length][filtered_array.length];

        //??????
        Array_Copy(filtered_array, filtered_array_copy);
        int idx = find_index_by_place("??????");

        Log.d("table", String.valueOf(idx));


        //icf.print_filtered_array();





        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //--------------------------------------------------code for restoring bundle------
        requestFlag = false;
        latitude = new ArrayList<Double>();
        longitude = new ArrayList<Double>();
        /* ui element*/
        distText = (TextView) findViewById(R.id.mapsActivity_textView_distance);
        start = (FloatingActionButton) findViewById(R.id.start);
        pause = (FloatingActionButton) findViewById(R.id.pause);
        stop = (FloatingActionButton) findViewById(R.id.stop);
        distanceView = (TextView) findViewById(R.id.mapsActivity_textView_distance);
        timeView = (TextView) findViewById(R.id.mapsActivity_textView_time);
        startImage = textImage("start");
        stopImage = textImage("stop");
        pauseImage = textImage("pause");
        playImage = textImage("play");
        start.setImageBitmap(startImage);
        stop.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.INVISIBLE);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        settingClient = LocationServices.getSettingsClient(this);

        createLocationRequest();
        locationRequestSetting();
        gettingLastLocation();
        Log.d("create called:", "inside create");



        // ==========================================================================??????






        // ==========================================================================

        startTravel_arrayList = new ArrayList<String>();
        startTravel_arrayList.add("?????????");
        startTravel_arrayList.add("??????");

        //============================================================

        startTravel_timearrayList = new ArrayList<String>();


        startTravel_datearrayList = new ArrayList<String>();
        startTravel_datearrayList.add("5???25???");
        startTravel_datearrayList.add("5???25???");

        //============================================================
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), PopupActivity.class);

                intent.putExtra("type", PopupType.IMAGE);
                intent.putExtra("title", "ImageUrl"); //Image
                intent.putExtra("buttonLeft", "????????? ????????????");
                intent.putExtra("buttonRight", "?????? ?????? ??????");
                startActivityForResult(intent, 4);

            }
        });


    }

    public void print_array() {
        for (int i = 0; i < table_place.length; i++) {
            for (int j = 0; j < table_user.length; j++) {
                System.out.print(array[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    //filtered_array ???????????? ??????
    public void print_filtered_array() {
        for (int i = 0; i < table_place.length; i++) {
            for (int j = 0; j < table_place.length; j++) {
                System.out.print(filtered_array[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public int find_index_by_place(String place_name) {
        for (int i = 0; i < table_place.length; i++) {
            if (place_name.equals(table_place[i])) {
                return i;
            }
        }
        return -1;
    }

    public int find_index_by_value(double[] arr, double val) {
        for (int i = 0; i < arr.length; i++) {
            if (val == arr[i]) {
                return i;
            }
        }
        return -1;
    }

    public void Array_Copy(double[][] arr, double[][] arr_copy) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr_copy[i][j] = arr[i][j];
            }
        }
    }

    public String[] rank_slice(String place_name, int rank) {
        //double[][] filtered_array_copy=Arrays.copyOf(filtered_array,filtered_array.length);
        double[][] filtered_array_copy = new double[filtered_array.length][filtered_array.length];

        //??????
        Array_Copy(filtered_array, filtered_array_copy);
        int idx = find_index_by_place(place_name);

        double[] cs_between_place_array = filtered_array_copy[idx];
        double[] cs_between_place_array_idxing = filtered_array[idx];

        //???????????? ??????
        Arrays.sort(cs_between_place_array);
        int arr_len = cs_between_place_array.length;

        //?????? rank??? ??????
        int[] idx_list = new int[rank];
        String[] recommend_list = new String[100];
        for (int i = arr_len - 1; i > arr_len - rank - 1; i--) {
            idx_list[i - arr_len + rank] = find_index_by_value(cs_between_place_array_idxing, cs_between_place_array[i]);
        }

        for (int i = idx_list.length - 1; i >= 0; i--) {
            // ?????? ??? ????????? : idx_list[i]
            System.out.println("slice: "+table_place[idx_list[i]]);
            recommend_list[rank-i-1] = table_place[idx_list[i]];
        }

        return recommend_list;
    }


    public void filtering() {



    }



    public void gettingLastLocation() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                if (lastLocation != null) {
                    float travelDistance = lastLocation.distanceTo(mCurrentLocation); //??????: m
                    distance = distance + travelDistance;
                    float viewDistance = distance / 1000; //??????: km
                    distanceView.setText(String.format("%.2f", viewDistance)+"km"); //????????? km??? ????????????
                    addPolyLine_onMap(mCurrentLocation); //????????? ???????????? update???. pin??? ?????? ??????
                } else {
                    pinMap(mCurrentLocation, 100); //?????? ?????? start ?????? ??? ??? ??????(MarkerList??? ??? ????????? ??????)
                    addPolyLine_onMap(mCurrentLocation); //?????? ?????? start ?????? ??? ??? ??????(MarkerList??? ??? ????????? ??????)
                }

            }
        };
    }


//method to convert your text to image

    public static Bitmap textImage(String text) {
        int textColor = Color.WHITE;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(16);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == 4){
                PopupResult result = (PopupResult) data.getSerializableExtra("result");
                if(result == PopupResult.LEFT){

                    for (int i = 0; i < startTravel_arrayList.size(); i++) {
                        if (i == 0) {
                            resultArrayList = resultArrayList + startTravel_arrayList.get(i);
                            resultdateArrayList = resultdateArrayList + startTravel_datearrayList.get(i);
                            resulttimeArrayList = resulttimeArrayList + startTravel_timearrayList.get(i);

                        } else {
                            resultArrayList = resultArrayList + "@" + startTravel_arrayList.get(i);
                            resultdateArrayList = resultdateArrayList + "@" + startTravel_datearrayList.get(i);
                            resulttimeArrayList = resulttimeArrayList + "@" + startTravel_timearrayList.get(i);
                        }
                    }


                    AboveModel aboveModel = new AboveModel();
                    aboveModel.uid = myUid;
                    aboveModel.path = resultArrayList;
                    aboveModel.secretCode = myUid + resultArrayList;
                    aboveModel.date = resultdateArrayList;
                    aboveModel.time = resulttimeArrayList;
                    aboveModel.state = "false";
                    aboveModel.uri = "";
                    aboveModel.buttonArray = "";

                    FirebaseDatabase.getInstance().getReference().child("Above_information")
                            .child(myUid + resultArrayList).setValue(aboveModel);

                    finish();
                } else if(result == PopupResult.RIGHT){
                    for (int i = 0; i < startTravel_arrayList.size(); i++) {
                        if (i == 0) {
                            resultArrayList = resultArrayList + startTravel_arrayList.get(i);
                            resultdateArrayList = resultdateArrayList + startTravel_datearrayList.get(i);
                            resulttimeArrayList = resulttimeArrayList + startTravel_timearrayList.get(i);

                        } else {
                            resultArrayList = resultArrayList + "@" + startTravel_arrayList.get(i);
                            resultdateArrayList = resultdateArrayList + "@" + startTravel_datearrayList.get(i);
                            resulttimeArrayList = resulttimeArrayList + "@" + startTravel_timearrayList.get(i);
                        }
                    }


                    AboveModel aboveModel = new AboveModel();
                    aboveModel.uid = myUid;
                    aboveModel.path = resultArrayList;
                    aboveModel.secretCode = myUid + resultArrayList;
                    aboveModel.date = resultdateArrayList;
                    aboveModel.time = resulttimeArrayList;
                    aboveModel.state = "ready";
                    aboveModel.uri = "";
                    aboveModel.buttonArray = "";

                    FirebaseDatabase.getInstance().getReference().child("Above_information")
                            .child(myUid + resultArrayList).setValue(aboveModel);


                    Intent intent = new Intent(getApplicationContext(), EvaluationActivity2.class);
                    intent.putExtra("startTravel_arrayList", startTravel_arrayList);
                    intent.putExtra("startTravel_timearrayList", startTravel_timearrayList);
                    intent.putExtra("startTravel_datearrayList", startTravel_datearrayList);
                    intent.putExtra("secret_code", myUid + resultArrayList);

                    startActivity(intent);
                    finish();
                }
            }
        }

        if (requestCode == startClickTriggered) {
            if (resultCode == Activity.RESULT_OK) {
                user = new userTable();
                user.gender = data.getStringExtra("gender");
                user.weight = Long.parseLong(data.getStringExtra("weight"));
                user.age = Long.parseLong(data.getStringExtra("age"));
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "User need to register",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();
            }
        }
        if (requestCode == registerTriggered) {
            if (resultCode == Activity.RESULT_OK) {
                if (user == null) {
                    user = new userTable();
                }
                user.gender = data.getStringExtra("gender");
                user.weight = Long.parseLong(data.getStringExtra("weight"));
                user.age = Long.parseLong(data.getStringExtra("age"));

            } else {
                // not needed
            }
        }


        if (requestCode == REQUEST_CODE){
            storename_selected = data.getStringExtra("storename_selected");
            //?????? ?????? ?????? index??? ?????? ???????????? ????????? index-1??? ?????? ??????
            MarkerList.get(onCount_markerList_index-1).set_storename_selected(storename_selected);
            markerArrayList.get(onCount_markerList_index).remove(); //title??? "????????? ??????... "??? ????????? ??????
            MarkerList.get(onCount_markerList_index-1).setMarkerTitle(storename_selected);
            markerOptionsArrayList.get(onCount_markerList_index).title(storename_selected);
            mMap.addMarker(markerOptionsArrayList.get(onCount_markerList_index));
            Toast.makeText(StartTravelActivity.this, MarkerList.get(onCount_markerList_index-1).storename_selected+"???(???) ?????????????????????.", Toast.LENGTH_SHORT).show();


            rankList = new ArrayList<>();
            rankList.clear();


            Intent intent = new Intent(getApplicationContext(), recommendActivity.class);

            intent.putExtra("storename_selected", storename_selected);
            intent.putExtra("rankList", rankList);
            startActivity(intent);

        }

    }


    /**
     * checking permission for the application:
     * checks whether user gives the permission to the application that is mentioned in the manifest
     **/

    private boolean checkingPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return (permission == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * requesting permission for the application
     * if not grantted by the user.
     **/

    private void requestPermission() {

        /* if user has rejected to accept the permission without choosing "never show again" option*/
        boolean should_show_detail = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (should_show_detail) {
            displaySnackbar(R.string.permission_request,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(StartTravelActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    request_validation);
                        }
                    });
        } else {
            /* requesting to user to give permission could be first time or when trying to use the app
             * even when "never ask was selected"*/
            ActivityCompat.requestPermissions(StartTravelActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    request_validation);
        }

    }


    /**
     * implementing callback for ActivityCompat
     **/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == request_validation) {
            if (permissions.length <= 0) {
                /* user ingnored, and ther permission was not set or denied*/
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /* permission granted*/
                //we can start our loaction work ---------------------------------------------------
                startLocationUpdate();
            } else {
                /*permission denied*/
                displaySnackbar(R.string.permission_denied, R.string.setting, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(StartTravelActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                request_validation);
                    }
                });
            }
        }
    }


    /**
     * displaying snackbar for the permission
     **/

    private void displaySnackbar(final int id, final int message, View.OnClickListener barclick) {
        Snackbar.make(findViewById(android.R.id.content), getString(id), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(message), barclick).show();
    }



    public void createLocationRequest() {
        mLocationrequest = new LocationRequest();
        mLocationrequest.setInterval(requestInterval);
        mLocationrequest.setFastestInterval(fastInterval);
        mLocationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationrequest.setSmallestDisplacement(minmumDistanceforRequest);
    }

    public void locationRequestSetting() {
        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationrequest);
    }


    /**
     * onResume
     **/

    @Override
    public void onResume() {
        Log.d("resume called:", "inside resume");
        super.onResume();
        if (activityStateIndicator) {
            if (startButtonClickedState) {
                if (checkingPermission()) {
                    activityStateIndicator = false;
                    startLocationUpdate();
                } else if (!checkingPermission()) {
                    activityStateIndicator = false;
                    requestPermission();
                }
            }
        }
    }


    /* on pause*/

    @Override
    public void onPause() {
        super.onPause();
        Log.d("pause state", "pause");
        activityStateIndicator = true;
    }



    /*
     * button click
     * */

    public void startClick(View view) {
        mMap.clear();
        timeView.setText("");
        distanceView.setText("0.00km");
        speedstate = true;

        onCount_markerList_index = 0;

        //MarkerList.clear(); //?????? ????????? ??? ?????? ?????? ??? ?????????



        polylineOption = new PolylineOptions().width(14).color(0xFFBB86FC).startCap(new RoundCap()).endCap(new RoundCap());
        stop.setImageBitmap(stopImage);
        pause.setImageBitmap(pauseImage);
        stop.setVisibility(View.VISIBLE);
        pause.setVisibility(View.VISIBLE);
        start.setVisibility(View.INVISIBLE);
        startButtonClickedState = true;
        if (checkingPermission()) {
            requestFlag = true;
            time1 = System.currentTimeMillis();
            timeUpdate = new UserService();
            timer = new Thread(timeUpdate);
            timer.start();

            startLocationUpdate();
            mMap.setMyLocationEnabled(true);  //????????????


        } else if (!checkingPermission()) {
            requestPermission();
        }



        Double Marker1_lat, Marker1_lng, Marker2_lat, Marker2_lng, Marker3_lat, Marker3_lng;

        //????????????
        Marker1_lat=37.5659240; Marker1_lng=126.975010;
        Marker2_lat=37.5662027; Marker2_lng=126.972146;
        MarkerList.add(new MarkerModel(Marker1_lat, Marker1_lng, "????????? ???????????? ????????????","??????????????? ?????? ???????????? 99", new ArrayList<String>(), ""));
        MarkerList.add(new MarkerModel(Marker2_lat,Marker2_lng,"????????? ???????????? ????????????","??????????????? ?????? ????????? 33", new ArrayList<String>(), ""));
        //        Distance sampledist = new Distance(Marker1_lat, Marker1_lng, Marker2_lat, Marker2_lng,"km");
//        Log.d("sampledist: ", Double.toString(sampledist.distance()));

    }



    public void stopClick(View view) {
        //Double speed = 0.0;
        stopLocationUpdates();
        stop.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.INVISIBLE);
        start.setVisibility(View.VISIBLE);
        userData userdata = new userData();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        Date date = new Date();
        userdata.setDate(dateFormat.format(date));
        if (timeView.getText() != "") {
            userdata.setTime(timeView.getText().toString());
        } else {
            userdata.setTime("0");
        }
        if (distanceView.getText() != "") {

            String distanceString = distanceView.getText().toString();
            Log.d("distanceString: ",distanceString.substring(0,distanceString.length()-2));
            userdata.setDistance(Double.parseDouble(distanceString.substring(0,distanceString.length()-2))); //distanceView??? 0.00km??? ???????????? ??????
        } else {
            userdata.setDistance(0.0);
        }
        if (distanceView.getText() != "" && timeView.getText() != "") {
            speed = (Double.parseDouble(distanceView.getText().toString().substring(0,distanceView.getText().toString().length()-2))) / (double) TimeUnit.MILLISECONDS.toMinutes(totalTime);
            speed = (double) Math.round(speed * 100) / 100;
            userdata.setSpeed(speed);
        } else {
            userdata.setSpeed(0);
        }
        //stop ????????? count??? ?????? ??????
        pinning.count_dist=0;
        MarkerList.clear(); //stop ????????? MarkerList ?????????? ????????? ??????

        lastLocation = null;
        distance = 0;
        totalTime = 0;
        calories = 0;
        if (threadstate)
            timer.interrupt();

    }



    public void pauseClick(View view) {
        if (count == 1) {
            pause.setImageBitmap(playImage);
            stopLocationUpdates();
            timer.interrupt();
            count = 0;
        } else {
            pause.setImageBitmap(pauseImage);
            if (checkingPermission()) {
                if (!threadstate) {
                    if (!timer.isAlive()) {
                        timer = new Thread(timeUpdate);
                        time1 = System.currentTimeMillis();
                        timer.start();
                    }
                }
                startLocationUpdate();
            } else if (!checkingPermission()) {
                requestPermission();
            }
            count = 1;
        }
    }


    /**
     * onStopLocation update
     **/

    @Override
    protected void onStop() {
        super.onStop();
        activityStateIndicator = true;
        Log.d("stop state", "state");
    }

    /**
     * onDestroy
     * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("distroy called:", "inside destroy");
        stopLocationUpdates();
        if (threadstate) {
            timer.interrupt();
        }
    }


    /**
     * startingLocation update
     **/

    public void startLocationUpdate() {
        task = settingClient.checkLocationSettings(builder.build());
        /**
         *  analysing the result of the task through callbacks
         * **/
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    /*according to the book we can call fused location object here but not in the code*/
                    if (checkingPermission()) {
                        mFusedLocationProviderClient.requestLocationUpdates(mLocationrequest,
                                mLocationCallback, null);
                    }
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(StartTravelActivity.this, removable_resolution);
                            } catch (IntentSender.SendIntentException intendException) {
                            } catch (ClassCastException castException) {
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String error = "Location settings cannot be fixed";
                            Toast.makeText(StartTravelActivity.this, error, Toast.LENGTH_LONG).show();
                            requestFlag = false;
                            break;
                    }
                }
                //    catch(SecurityException e){}
            }
        });
    }

    /**
     * stoping update of loaction
     **/

    public void stopLocationUpdates() {
        if (!requestFlag) {
            return;
        } else {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            requestFlag = false;
                            Log.d("stop location called", "remove location");
                        }

                    });
        }
    }




/**
 * location update for continues trigger
 * **/


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) { //?????? ????????? ?????? ???
        mMap = googleMap;

        LatLng temp = new LatLng(37, 126);
        zoom= CameraUpdateFactory.zoomTo(18);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(temp));
        center=CameraUpdateFactory.newLatLng(temp);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);




    }


    public double check_distance_every_second(Location current_location, Location last_location) {

        if (current_location != null & last_location != null) {
            Distance distance_every_second = new Distance(current_location.getLatitude(), current_location.getLongitude(), last_location.getLatitude(), last_location.getLongitude(), "km");
            Log.d("current_distance", Double.toString(distance_every_second.distance()));

            return distance_every_second.distance();
        } else {
            return 0;
        }
    }


    public void zoomMap(Location location) { //????????? ?????? ????????? ?????? : polylline ????????? ????????? ???????????? ?????????
        //Log.d(TAG, "updateMap : ");


    }



    public void addPolyLine_onMap(Location location) {
        Log.d("addPolyLine_onMap", "here");

        lastLocation = mCurrentLocation;

        latLng = new LatLng(location.getLatitude(), location.getLongitude());

        polylineOption.add(latLng);
        polyline = mMap.addPolyline(polylineOption);
        center=CameraUpdateFactory.newLatLng(latLng);


        mMap.moveCamera(center); // ?????? ????????? ?????? ????????? ??????
        //mMap.animateCamera(zoom);

    }


    public void pinMap(Location location, int i) { //location ????????? ??????

        addPolyLine_onMap(location);

        //?????? ????????? ?????? ????????? ????????? ?????? ????????? ????????? ????????? ??????
//        String markerTitle = getCurrentAddress(latLng);
//        URLaddress = markerTitle.replaceAll(" ","+"); //URL??? ?????? ?????? ??????(?????????+?????????+ ...)
//        String markerSnippet = "??????:" + String.valueOf(location.getLatitude())
//                + " ??????:" + String.valueOf(location.getLongitude());

        //?????? ????????? ?????? ???????????? ??????
//        getSampleMarkerItems(currentPosition.latitude, currentPosition.longitude, markerTitle, markerSnippet);

        //????????????
        if (onCount_markerList_index < 2) {
            URLaddress = MarkerList.get(onCount_markerList_index).getMarkerTitle().replaceAll(" ", "+"); //URL??? ?????? ?????? ??????(?????????+?????????+ ...)
        }

        mCurrentLocation = location; //????????? ??????.
        LatLng position;
        String markerTitle,markerSnippet;

        Log.d("Markeroptions","markeroptions"+Integer.toString(i));

        MarkerOptions markerOptions = new MarkerOptions();

        //????????????
        Bitmap smallMarker;
        Bitmap bitmap;
        BitmapDrawable bitmapDrawable;

        if (i==100) {
            //Toast.makeText(this, "StartMap", Toast.LENGTH_SHORT).show();
            position = new LatLng(location.getLatitude(), location.getLongitude());
            markerTitle = "";
            markerSnippet = "";

            bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.traveler_icon);
            bitmap= bitmapDrawable.getBitmap();
            smallMarker = Bitmap.createScaledBitmap(bitmap, 125, 125, false);


        } else {
            Toast.makeText(this,"????????? ???????????? ????????????", Toast.LENGTH_LONG).show();
            Log.d("MarkerLat",Double.toString(MarkerList.get(i).getLat()));
            position = new LatLng(MarkerList.get(i).getLat(), MarkerList.get(i).getLng());
            markerTitle = MarkerList.get(i).getMarkerTitle();
            markerSnippet = MarkerList.get(i).getMarkerSnippet();
            Log.d("markerSnippet: ", markerSnippet);
            //Log.d("URLaddress", URLaddress);    URLaddress ????????? ????????? marker??? ????????? ???

            bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.origami_thin);
            bitmap= bitmapDrawable.getBitmap();
            //origami??? 130, plane_ani??? 100
            smallMarker = Bitmap.createScaledBitmap(bitmap, 130, 130, false);
        }

        //??? ???????????? ????????? ??????
        Bitmap bitmap2 = addShadow(smallMarker, smallMarker.getHeight(), smallMarker.getWidth(), 20, -10, 25, Color.DKGRAY, 80);


        markerOptions.title(markerTitle);
        markerOptions.position(position);
        //markerOptions.anchor(0.5,0.5);
        markerOptions.snippet(markerSnippet);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap2));


        //markerOptionsArrayList.add


        Marker marker = mMap.addMarker(markerOptions);
        markerArrayList.add(marker);
        markerOptionsArrayList.add(markerOptions);
        mMap.setOnMarkerClickListener(this);




        Log.d("markerlistlength",Integer.toString(MarkerList.size()));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        //markerArrayList.get(onCount_markerList_index).remove(); //????????? ??? ????????? ?????? ???????????? index 0????????? index ?????? ??? ?????? ??????
        Intent intent = new Intent(this, StoreListActivity.class);
        System.out.println("storename_list"+Integer.toString(onCount_markerList_index-1)+MarkerList.get(onCount_markerList_index-1).storename_list);
        intent.putStringArrayListExtra("storename_list", MarkerList.get(onCount_markerList_index-1).storename_list);


        if (MarkerList.get(onCount_markerList_index-1).storename_selected == ""){
            startActivityForResult(intent, REQUEST_CODE);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
        else {
            //Toast.makeText(this, "storename: "+MarkerList.get(onCount_markerList_index-1).storename_selected+", index: "+Integer.toString(onCount_markerList_index),Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean fadeMarker(final Marker marker) {
        //Make the marker fade
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            float f;
            int i = 2;
            int timer;
            @Override
            public void run() {
                if(i%2 == 0)
                {
                    f = 1f;
                }
                else
                    f = 0f;
                marker.setAlpha(f);
                i++;
                timer = timer + 300;
                if(timer < 30){
                    handler.postDelayed(this, 300);
                }
                else marker.remove();
            }
        });
        return true;
    }

    //????????????
    public Bitmap addShadow(final Bitmap bm, final int dstHeight, final int dstWidth, int size, float dx, float dy, int color, int alpha) {
        final Bitmap mask = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ALPHA_8);
        final Matrix scaleToFit = new Matrix();
        final RectF src = new RectF(0, 0, bm.getWidth(), bm.getHeight());
        final RectF dst = new RectF(0, 0, dstWidth - dx, dstHeight - dy);
        scaleToFit.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
        final Matrix dropShadow = new Matrix(scaleToFit);

        dropShadow.postTranslate(dx, dy);
        final Canvas maskCanvas = new Canvas(mask);
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskCanvas.drawBitmap(bm, scaleToFit, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        maskCanvas.drawBitmap(bm, dropShadow, paint);
        final BlurMaskFilter filter = new BlurMaskFilter(size, BlurMaskFilter.Blur.SOLID);
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setMaskFilter(filter);
        paint.setFilterBitmap(true);
        final Bitmap ret = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888);
        final Canvas retCanvas = new Canvas(ret);
        retCanvas.drawBitmap(mask, 0, 0, paint);
        retCanvas.drawBitmap(bm, scaleToFit, null);
        mask.recycle();
        return ret;
    }

    private void animateMarker(Marker marker) {
        final Handler handler = new Handler();

        final long startTime = SystemClock.uptimeMillis();
        final long duration = 3000000; // ms

        Projection proj = mMap.getProjection();
        final LatLng markerLatLng = marker.getPosition();
        Point startPoint = proj.toScreenLocation(markerLatLng);
        startPoint.offset(0, -10);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * markerLatLng.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * markerLatLng.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later (60fps)
                    handler.postDelayed(this, 400);
                }
            }
        });
    }





    /**
     * Code to update google map
     */

    public void updateMap(Location location) { //????????? ?????? ????????? ?????? : polylline ????????? ????????? ???????????? ?????????
        //Log.d(TAG, "updateMap : ");


        textView.setText(get_storeName); //?????? ??????

        lastLocation = mCurrentLocation;
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        polylineOption.add(latLng);

        polyline = mMap.addPolyline(polylineOption);
        center=CameraUpdateFactory.newLatLng(latLng);
        mMap.moveCamera(center); // ?????? ????????? ?????? ????????? ??????
        mMap.animateCamera(zoom);
    }




    //MarkerModel ???????????? ?????? ?????? ????????????
    private void getSampleMarkerItems(double lat, double lng, String title, String snippet) {

        //MarkerList.add(new MarkerModel(lat, lng, title, snippet));
//        MarkerList.add(new MarkerModel(37.5444445,126.9447639, "??????+?????????+?????????43???+12","marker1"));
//        MarkerList.add(new MarkerModel(37.5444445,126.9447639, "???????????????+?????????+????????????+21","marker2"));

        for (MarkerModel markerModel : MarkerList) {
            Toast.makeText(this,"Marker Title:"+markerModel.markerTitle, Toast.LENGTH_SHORT).show();
            Log.d("MarkerModel",markerModel.markerTitle);
            LatLng position = new LatLng(markerModel.getLat(), markerModel.getLng());
            String markerTitle = markerModel.getMarkerTitle();
            String markerSnippet = markerModel.getMarkerSnippet();

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(markerTitle);
            markerOptions.position(position);
            markerOptions.snippet(markerSnippet);

            mMap.addMarker(markerOptions);
            // markerModel.show_latlng();   :???
        }

        Log.d("markerlistlength",Integer.toString(MarkerList.size()));

    }


    public String getCurrentAddress(LatLng latlng) {
        //????????????... GPS??? ????????? ??????
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
            //Toast.makeText(this, "???????????? ????????? ??????", Toast.LENGTH_LONG).show();
        } catch (IOException ioException) {
            //???????????? ??????
            Toast.makeText(this, "???????????? ????????? ????????????", Toast.LENGTH_LONG).show();
            return "???????????? ????????? ????????????";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "????????? GPS ??????", Toast.LENGTH_LONG).show();
            return "????????? GPS ??????";
        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "?????? ?????????", Toast.LENGTH_LONG).show();
            return "?????? ?????????";

        } else {
            Address address = addresses.get(0);
            Log.d(TAG, "onComplete: "+addresses.get(0).getAddressLine(0));
            return address.getAddressLine(0);
        }

    }

    /*** thread for running timer*/

    /*** thread for running timer*/

    public class UserService implements Runnable{
        @Override
        public void run(){
            Log.d("inside runable"," Thread running");
            Thread.currentThread().setName("UserService");
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            threadstate = true;
            while(StartTravelActivity.requestFlag) {
                long time2 = System.currentTimeMillis();
                StartTravelActivity.totalTime = StartTravelActivity.totalTime + (time2 - time1);
                time1 = time2;
                long second = (StartTravelActivity.totalTime / 1000) % 60;
                long minute = (StartTravelActivity.totalTime / (60 * 1000)) % 60;
                long hour = (StartTravelActivity.totalTime / (60 * 60 * 1000)) % 24;
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putLong("hours",hour);
                bundle.putLong("minute",minute);
                bundle.putLong("second",second);
                message.setData(bundle);
                MainHandler.sendMessage(message);
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException irexcption) {
                    Log.d("inside runnable","Interrupted");
                    threadstate = false;
                    break;
                }
            }
        }
    }


    @SuppressLint("HandlerLeak")
    public Handler MainHandler = new Handler(){
        public void handleMessage(Message inputMessage){ //1????????? ???????????? ?????????
            Bundle bundle = inputMessage.getData();
            String hour = Long.toString(bundle.getLong("hours"));
            String minute = Long.toString(bundle.getLong("minute"));
            String second = Long.toString(bundle.getLong("second"));
            timeView.setText(hour+":"+minute+":"+second);

//
//            if (mCurrentLocation!=null) {
//                Log.d("if???", "if??? before checkdistance");
//                check_distance_every_second(mCurrentLocation,lastLocation);
//                if(check_distance_every_second(mCurrentLocation,lastLocation) < 0.0001) {
//                    addPolyLine_onMap(mCurrentLocation);
//                    //zoomMap(mCurrentLocation);
//
//                } //polyline ?????? ?????????
//            }

            gettingLastLocation();


            Distance distance_every_second;
            if (mCurrentLocation != null & lastLocation != null) {
                distance_every_second = new Distance(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), lastLocation.getLatitude(), lastLocation.getLongitude(), "km");
                Log.d("current_distance", Double.toString(distance_every_second.distance()));
            }


            if (mCurrentLocation!=null) pinning.setCurrent(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
            //????????????
            if (onCount_markerList_index < 2){
                pinning.check_radius(MarkerList.get(onCount_markerList_index)); //2km?????? ??? ??? ?????? ??? ???????????? ??????&?????? ????????? or ????????? ??????
                Log.d("pinning", Integer.toString(pinning.count_dist)); //count_dist: ?????? ???????????? ?????? ????????? ?????????
                //Toast.makeText(getApplicationContext(), Integer.toString(pinning.count_dist), Toast.LENGTH_SHORT).show();
                if ((pinning.count_dist == 30 ) & (pinning.count_dist > 0)) { //count_dist??? 5??? ??? ????????? ??????
                    Log.d("onCountmarkerList_index", Integer.toString(onCount_markerList_index));
                    onCount(pinning);
                    onCount_markerList_index += 1; //?????? ???????????? ???. ????????? ???????????? ??? ????????? ????????? ??????

                     //?????? ?????? ????????????
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm");

                    String getTime = sdf.format(date);
                    if (onCount_markerList_index > 0) {
                        startTravel_timearrayList.add(getTime);
                        for (int index = 0; index < startTravel_timearrayList.size(); index++) {
                            //Toast.makeText(StartTravelActivity.this, startTravel_timearrayList.get(index), Toast.LENGTH_SHORT).show();
                            System.out.println("time: " + startTravel_timearrayList.get(index));
                        }
                    }

                    pinning.count_dist = 0;
                } else {
                    //Log.d("pinning",Integer.toString(pinning.count_dist));
                }
            }
        }
    };



    public void onCount(DistanceRadius pin) {
        Log.d("oncount","oncount");
        new CrawlingThread().execute();

        Log.d("markerlistlength",Integer.toString(MarkerList.size()));
        //Log.d("Pinning Count: ", Integer.toString(pinning.count_dist));

//        Integer markerList_index = (pin.count_dist)/5 +1;
        //Integer markerList_index = (pin.count_dist)/5;

        pinMap(mCurrentLocation, onCount_markerList_index); //??? ??????
        //Log.d("markerList_index: ",Integer.toString(markerList_index));

//        pin.setCurrent(MarkerList.get(markerList_index).lat,MarkerList.get(markerList_index).lng);
        pin.printLatest(); //?????? latlng ??????

    }



    private class CrawlingThread extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Log.d("CrawlingThread","CrawlingThread"); //ok
                String URL = "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&query="+MarkerList.get(onCount_markerList_index).getMarkerSnippet();
                //String URL = "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&query=??????+?????????+?????????43???+12";
                Log.d("URL in CrawlingThread",URL);
                Document doc = Jsoup.connect(URL).get();
                Elements contents;
                contents = doc.select("div._1toz7 span._309N9"); //span._309N9: ?????? ???????????? ??????

                for (Element e : contents) { //URL??? ?????? ?????? ??? ?????? ??? ?????????
                    Log.d("Debug","for???");
                    get_storeName = e.text();
                    System.out.println("get_storeName: "+get_storeName + "\n");
                    MarkerList.get(onCount_markerList_index-1).add_storename_list(get_storeName);
                    //textView ????????? mainThread????????? ??? ??? ??????.
                }
            } catch (IOException e) {
                Log.d("Debug","catch");
                e.printStackTrace();
            }
            return null;
        }
    }

}