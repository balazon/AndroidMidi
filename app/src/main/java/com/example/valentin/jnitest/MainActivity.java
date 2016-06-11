package com.example.valentin.jnitest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import org.billthefarmer.mididriver.GeneralMidiConstants;
import org.billthefarmer.mididriver.MidiConstants;
import org.billthefarmer.mididriver.MidiDriver;

import java.util.Timer;

import static org.billthefarmer.mididriver.MidiConstants.PROGRAM_CHANGE;

public class MainActivity extends AppCompatActivity implements MidiDriver.OnMidiStartListener{

	static {
		System.loadLibrary("midi");
	}

    MidiDriver midi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "asdasd" , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


	    Button b = (Button)findViewById(R.id.pressmebutton);
	    b.setOnTouchListener(new View.OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
			    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
				    Log.d("TouchTest", "Touch down");
				    noteOn(60);
			    } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
				    Log.d("TouchTest", "Touch up");
				    noteOff(60);
			    }
			    return true;
		    }
	    });

        midi = new MidiDriver();
        if(midi.start()) {
	        Log.d("ERR", "START FAIL");
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }







	protected void noteOn(int pitch) {
		int channel = 1;
		int velocity = 127;

		sendMidi(MidiConstants.NOTE_ON + (channel - 1), pitch, velocity);
	}

	protected void noteOff(int pitch) {
		int channel = 1;
		sendMidi(MidiConstants.NOTE_OFF + (channel - 1), pitch, 0);
	}

	@Override
	public void onMidiStart() {
		sendMidi(MidiConstants.PROGRAM_CHANGE, GeneralMidiConstants.ACOUSTIC_GRAND_PIANO);
	}


	protected void sendMidi(int m, int p) {
		byte[] msg = new byte[2];
		msg[0] = (byte) m;
		msg[1] = (byte) p;
		midi.write(msg);
	}

	protected void sendMidi(int m, int n, int v)
	{
		byte msg[] = new byte[3];

		msg[0] = (byte) m;
		msg[1] = (byte) n;
		msg[2] = (byte) v;

		midi.write(msg);
	}


}
