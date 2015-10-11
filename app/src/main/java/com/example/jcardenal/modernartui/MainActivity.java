package com.example.jcardenal.modernartui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.widget.SeekBar;


public class MainActivity extends AppCompatActivity {

    public static String TAG = "ModernArtUI";
    private final static int REGS = 5;
    private final static int DELTA = 9;
    private static SurfaceView regs[];
    private static String cols[] = {"#3cff33", "#0f06c4", "#94a9ff","#68ffdb","#f7fff8"}; // last one is assumed to be gray
    private static final String URL = "http://www.moma.org";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get a handle on  colored regions
        regs = new SurfaceView[REGS];
        regs[0] = (SurfaceView)findViewById(R.id.reg0);
        regs[1] = (SurfaceView)findViewById(R.id.reg1);
        regs[2] = (SurfaceView)findViewById(R.id.reg2);
        regs[3] = (SurfaceView)findViewById(R.id.reg3);
        regs[4] = (SurfaceView)findViewById(R.id.reg4);

        // assign initial colors
        for (int i=0; i<REGS; i++)
            regs[i].setBackgroundColor(Color.parseColor(cols[i]));

        // set listener for seek bar
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(TAG,"Progress: "+progress+" fromUser: "+fromUser);
                if (fromUser) {
                    for (int i = 0; i < REGS - 1; i++) // last one is gray and doesn't change
                        regs[i].setBackgroundColor(Color.parseColor(cols[i]) + DELTA * progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i(TAG,"Start Tracking");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i(TAG, "Stop Tracking");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.more_info) {
            MoreInfoDialogFragment.newInstance().show(getFragmentManager(), "Alert");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
        DIALOG
     */

    // Class that creates the AlertDialog
    public static class MoreInfoDialogFragment extends DialogFragment {

        public static MoreInfoDialogFragment newInstance() {
            return new MoreInfoDialogFragment();
        }

        // Build AlertDialog using AlertDialog.Builder
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage("Inspired by the works of artists such as Piet Mondrian and Ben Nicholson.\n\nClick below to learn more!")

                            // User cannot dismiss dialog by hitting back button
                    .setCancelable(false)


                            // Set up No Button
                    .setNegativeButton("Not Now",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    //Do nothing
                                }
                            })

                            // Set up Yes Button
                    .setPositiveButton("Visit MOMA",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        final DialogInterface dialog, int id) {
                                    // Create intent (implicit) to invoke browser-capable app
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                                    startActivity(intent);
                                }
                            }).create();
        }
    }
}
