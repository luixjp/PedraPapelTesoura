package br.edu.ifsp.scl.sdm.pedrapapeltesoura;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Random;

import br.edu.ifsp.scl.sdm.pedrapapeltesoura.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Referencias para objetos de UI de leioute
    private ActivityMainBinding activityMainBinding;
    private ActivityResultLauncher<Intent> configuracoesAcitivityResultLauncher;

    public static final String IS_TRIO = "br.edu.ifsp.scl.sdm.pedrapapeltesoura.MainActivity.IS_TRIO";
    public static final String NR_RODADAS = "br.edu.ifsp.scl.sdm.pedrapapeltesoura.MainActivity.NR_RODADAS";
    private boolean isTrio = false;
    private int nrRodadas = 1;
    private int loopRodadas = 0;
    private int qtdPontosJogador;
    private int qtdPontosComputadores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.btnPedra.setOnClickListener(this);
        activityMainBinding.btnPapel.setOnClickListener(this);
        activityMainBinding.btnTesoura.setOnClickListener(this);


        configuracoesAcitivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            isTrio = intent.getBooleanExtra(IS_TRIO, false);
                            nrRodadas = intent.getIntExtra(NR_RODADAS, 1);
                            loopRodadas = nrRodadas-1;
                            configureGame();
                        }
                    }
                });

        configureGame();
    }

    private void configureGame() {

        //clean up
        qtdPontosJogador = 0;
        qtdPontosComputadores = 0;
        activityMainBinding.ivJogadaComputador1.setImageResource(android.R.color.transparent);
        activityMainBinding.ivJogadaComputador2.setImageResource(android.R.color.transparent);
        activityMainBinding.tvResultado.setText("");
        activityMainBinding.tvQuantidadeRodadas.setText(getString(R.string.qtdRodadas) + " " + nrRodadas);

        //set visibility
        activityMainBinding.tvJogadaComputador2.setVisibility(isTrio ? View.VISIBLE : View.GONE);
        activityMainBinding.ivJogadaComputador2.setVisibility(isTrio ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miConfiguracoes: {
                Intent intent = new Intent(this, ConfiguracoesActivity.class);
                intent.putExtra(IS_TRIO, isTrio);
                intent.putExtra(NR_RODADAS, nrRodadas);
                configuracoesAcitivityResultLauncher.launch(intent);
                return true;
            }
            default:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        String jogada = "Nulo";

        switch (view.getId()) {
            case R.id.btnPedra:
                jogada = "Pedra";
                break;
            case R.id.btnPapel:
                jogada = "Papel";
                break;
            case R.id.btnTesoura:
                jogada = "Tesoura";
                break;
            default:
                break;
        }
        jogarJoKenPo(jogada);
    }


    private void jogarJoKenPo(String jogada) {

        StringBuilder resultadoSb = new StringBuilder();
        resultadoSb.append("Sua jogada: ");
        resultadoSb.append(jogada);
        resultadoSb.append(", Jogada do computador 1: ");

        Random rnd = new Random(System.currentTimeMillis());
        int jogadaComputador1 = rnd.nextInt(3);
        int jogadaComputador2 = rnd.nextInt(3);

        //Setando imagem da jogada do computador 1
        int imagemJogadaComputadorId = -1;
        switch (jogadaComputador1) {
            case 0: {
                imagemJogadaComputadorId = R.mipmap.pedra;
                resultadoSb.append("Pedra");
                break;
            }
            case 1: {
                imagemJogadaComputadorId = R.mipmap.papel;
                resultadoSb.append("Papel");
                break;
            }
            case 2: {
                imagemJogadaComputadorId = R.mipmap.tesoura;
                resultadoSb.append("Tesoura");
                break;
            }
            default:
                break;
        }

        activityMainBinding.ivJogadaComputador1.setImageResource(imagemJogadaComputadorId);

        if (isTrio) {

            resultadoSb.append(" , Jogada do computador 2: ");

            //Setando imagem da jogada do computador 2
            int imagemJogadaComputador2Id = -1;
            switch (jogadaComputador2) {
                case 0: {
                    imagemJogadaComputador2Id = R.mipmap.pedra;
                    resultadoSb.append("Pedra");
                    break;
                }
                case 1: {
                    imagemJogadaComputador2Id = R.mipmap.papel;
                    resultadoSb.append("Papel");
                    break;
                }
                case 2: {
                    imagemJogadaComputador2Id = R.mipmap.tesoura;
                    resultadoSb.append("Tesoura");
                    break;
                }
                default: {
                    break;
                }
            }
            activityMainBinding.ivJogadaComputador2.setImageResource(imagemJogadaComputador2Id);
        }


        analisarResultado(resultadoSb, jogada, jogadaComputador1, jogadaComputador2);
    }

    private void analisarResultado(StringBuilder resultadoSb, String jogada, int jogadaComputador1, int jogadaComputador2) {


        String status1 = verificaStatus(jogada, jogadaComputador1);

        if(!isTrio) {
            resultadoSb.append(". Você " + status1);

            switch (status1) {
                case "Ganhou!":
                    qtdPontosJogador++;
                    break;
                case "Perdeu!":
                    qtdPontosComputadores++;
                    break;
                default:
                    break;
            }
        }
        else {
            resultadoSb.append(". Contra o Computador 1, Você " + status1);

            String status2 = verificaStatus(jogada, jogadaComputador2);

            resultadoSb.append(" Contra o Computador 2, Você " + status2);

            if(status1.equals("Perdeu!") && status2.equals("Perdeu!")) {
                resultadoSb.append(" Resultado Final: Você Perdeu para os Computadores!");
                qtdPontosComputadores++;
            }
            else if(status1.equals("Empatou!") && status2.equals("Empatou!")) {
                resultadoSb.append(" Resultado Final: Você Empatou com os Computadores!");
            }
            else if(status1.equals("Ganhou!") && status2.equals("Ganhou!")) {
                resultadoSb.append(" Resultado Final: Parabéns Você Ganhou dos Computadores!");
                qtdPontosJogador++;
            }
            else if(status1.equals("Ganhou!") && status2.equals("Empatou!") ||
                    status2.equals("Ganhou!") && status1.equals("Empatou!")) {
                resultadoSb.append(" Resultado Final: Parabéns Você Ganhou dos Computadores!");
                qtdPontosJogador++;
            }
            else if(status1.equals("Ganhou!") && status2.equals("Perdeu!") ||
                    status2.equals("Ganhou!") && status1.equals("Perdeu!")) {
                resultadoSb.append(" Resultado Final: Você Empatou com dos Computadores!");
            }
            else if(status1.equals("Empatou!") && status2.equals("Perdeu!") ||
                    status2.equals("Empatou!") && status1.equals("Perdeu!")) {
                resultadoSb.append(" Resultado Final: Você Perdeu dos Computadores!");
                qtdPontosComputadores++;
            }
            else {
                resultadoSb.append(" Resultado Impossível!");
            }
        }

        activityMainBinding.tvResultado.setText(resultadoSb.toString());

        verificarRodadas();
    }

    private String verificaStatus(String jogada, int jogadaComputador) {

        String status = "";

        if (jogada.equals("Pedra")) {

            switch (jogadaComputador) {
                case 0:
                    //Pedra X Pedra
                    status = "Empatou!";
                    break;
                case 1:
                    //Pedra x Papel
                    status = "Perdeu!";
                    break;
                case 2:
                    //Pedra x Tesoura
                    status = "Ganhou!";
                    break;
                default:
                    break;
            }
        }

        if (jogada.equals("Papel")) {

            switch (jogadaComputador) {
                case 0:
                    //Papel X Pedra
                    status = "Ganhou!";
                    break;
                case 1:
                    //Papel x Papel
                    status = "Empatou!";
                    break;
                case 2:
                    //Papel x Tesoura
                    status = "Perdeu!";
                    break;
                default:
                    break;
            }
        }

        if (jogada.equals("Tesoura")) {

            switch (jogadaComputador) {
                case 0:
                    //Tesoura X Pedra
                    status = "Perdeu!";
                    break;
                case 1:
                    //Tesoura x Papel
                    status = "Ganhou!";
                    break;
                case 2:
                    //Tesoura x Tesoura
                    status = "Empatou!";
                    break;
                default:
                    break;
            }
        }

        return status;
    }

    private void verificarRodadas() {
        switch (nrRodadas) {
            case 1:
                break;
            case 3:
            case 5: {
                StringBuilder resultadoRodada = new StringBuilder();
                resultadoRodada.append(activityMainBinding.tvResultado.getText());
                resultadoRodada.append("\n");
                if (loopRodadas > 0) {
                    resultadoRodada.append("===== FIM RODADA NR: ");
                    resultadoRodada.append(nrRodadas-loopRodadas);
                    resultadoRodada.append("=====\n");
                    activityMainBinding.tvResultado.setText(resultadoRodada.toString());
                    loopRodadas--;
                }
                else {

                    resultadoRodada.append("===== FIM DE JOGO =====\n");
                    resultadoRodada.append("Seus pontos foram: " + qtdPontosJogador);
                    resultadoRodada.append("\nPontos do(s) Computador(es): " + qtdPontosComputadores);
                    resultadoRodada.append("\nJogadas em Empate: " + (nrRodadas-(qtdPontosComputadores+qtdPontosJogador)));

                    if(qtdPontosJogador > qtdPontosComputadores) {
                        resultadoRodada.append("\nParabéns! Na disputa por rodadas vocês ganhou!!!");
                    }
                    else if (qtdPontosJogador < qtdPontosComputadores) {
                        resultadoRodada.append("\nQue pena! Na disputa por rodadas vocês perdeu!!!");
                    }
                    else {
                        resultadoRodada.append("\nNa disputa por rodadas deu empate!!! Tente novamente!");
                    }
                    activityMainBinding.tvResultado.setText(resultadoRodada.toString());

                    //Fim de jogo, reiniciar jogo
                    loopRodadas = nrRodadas-1;
                    qtdPontosJogador = 0;
                    qtdPontosComputadores = 0;
                }
                break;
            }
            default:
                break;

        }
    }
}

