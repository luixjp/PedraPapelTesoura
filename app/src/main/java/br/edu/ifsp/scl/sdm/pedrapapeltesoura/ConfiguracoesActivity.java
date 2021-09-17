package br.edu.ifsp.scl.sdm.pedrapapeltesoura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import br.edu.ifsp.scl.sdm.pedrapapeltesoura.databinding.ActivityConfiguracoesBinding;
import br.edu.ifsp.scl.sdm.pedrapapeltesoura.databinding.ActivityMainBinding;

public class ConfiguracoesActivity extends AppCompatActivity {

    private ActivityConfiguracoesBinding activityConfiguracoesBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityConfiguracoesBinding = ActivityConfiguracoesBinding.inflate(getLayoutInflater());
        setContentView(activityConfiguracoesBinding.getRoot());

        //ActionBar
        getSupportActionBar().setTitle("Configurações");

        Intent configIntent = getIntent();
        if(configIntent != null) {
            int nr = configIntent.getIntExtra(MainActivity.NR_RODADAS, 1);
            boolean isTrio = configIntent.getBooleanExtra(MainActivity.IS_TRIO, false);

            activityConfiguracoesBinding.trioRb.setChecked(isTrio);
            switch (nr) {
                case 1:
                    activityConfiguracoesBinding.opcao1rodada.setChecked(true);
                    break;
                case 3:
                    activityConfiguracoesBinding.opcao3rodadas.setChecked(true);
                    break;
                case 5:
                    activityConfiguracoesBinding.opcao5rodadas.setChecked(true);
                    break;
                default:
                    break;
            }
        }


        activityConfiguracoesBinding.salvarBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent retornoIntent = new Intent();
                retornoIntent.putExtra(MainActivity.IS_TRIO, activityConfiguracoesBinding.trioRb.isChecked());
                int nr_rodadas = 1;
                if(activityConfiguracoesBinding.opcao3rodadas.isChecked())
                    nr_rodadas = 3;
                if(activityConfiguracoesBinding.opcao5rodadas.isChecked())
                    nr_rodadas = 5;
                retornoIntent.putExtra(MainActivity.NR_RODADAS, nr_rodadas);

                setResult(RESULT_OK, retornoIntent);
                finish();
            }
        });

        activityConfiguracoesBinding.cancelarBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        activityConfiguracoesBinding.stmMostraOpcoes.setOnCheckedChangeListener((compoundButton, mostrarOpcoes) -> {
            activityConfiguracoesBinding.llOpcoes.setVisibility(mostrarOpcoes ? View.VISIBLE : View.GONE);
            activityConfiguracoesBinding.llRodadas.setVisibility(mostrarOpcoes ? View.VISIBLE : View.GONE);
        });

    }
}