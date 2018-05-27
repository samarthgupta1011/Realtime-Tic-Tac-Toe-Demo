package neetiayog.samarthgupta.com.tictacfirebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameAltActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    Button btUpdate;
    String code;
    TextView tvLoading, tvTurn;
    Game game;
    boolean isHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_alt);

        tvLoading = (TextView) findViewById(R.id.tv_loading);
        tvTurn = (TextView) findViewById(R.id.tv_turn);
        btUpdate = (Button) findViewById(R.id.bt_last_move);
        btUpdate.setOnClickListener(this);
        btUpdate.setClickable(false);
        tvTurn.setVisibility(View.INVISIBLE);

        isHost = getIntent().getBooleanExtra("isHost", false);


        code = getIntent().getStringExtra("code");
        code = "9582184794";
        databaseReference.child(code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                game = dataSnapshot.getValue(Game.class);
    
                Log.i("CODE","5");
                if (game != null && game.isStarted) {

                    boolean hostTurn = game.host.turn;
                    boolean awayTurn = game.away.turn;

                    if (isHost && hostTurn) {
                        Log.i("CODE", "1");
                        tvTurn.setVisibility(View.VISIBLE);
                        btUpdate.setClickable(true);
                    } else if(isHost && awayTurn) {
                        Log.i("CODE", "2");
                        tvTurn.setVisibility(View.INVISIBLE);
                        btUpdate.setClickable(false);
                    } else if (!isHost && awayTurn) {
                        Log.i("CODE", "3");
                        tvTurn.setVisibility(View.VISIBLE);
                        btUpdate.setClickable(true);
                    } else if(!isHost && hostTurn) {
                        Log.i("CODE", "4");
                        tvTurn.setVisibility(View.INVISIBLE);
                        btUpdate.setClickable(false);
                    }

                } else {
                    Toast.makeText(GameAltActivity.this, "Game is null", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View view) {

        if (view == btUpdate) {

            if (game != null) {

                if (isHost) {

                    Log.i("CODE","6");
                    game.lastMove = 100;
                    game.host.setTurn(false);
                    game.away.setTurn(true);
                    databaseReference.child(code).setValue(game);
                    tvTurn.setVisibility(View.INVISIBLE);

                } else {

                    Log.i("CODE","7");
                    game.lastMove = 9;
                    game.host.setTurn(true);
                    game.away.setTurn(false);
                    databaseReference.child(code).setValue(game);
                    tvTurn.setVisibility(View.INVISIBLE);
                }

            }


        }

    }
}
