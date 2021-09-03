package br.edu.ifsp.scl.sdm.pedrapapeltesoura;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.Random;

import br.edu.ifsp.scl.sdm.pedrapapeltesoura.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Referencias para objetos de UI de leioute
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        //setContentView(R.layout.activity_main);

        activityMainBinding.btnPedra.setOnClickListener(this);
        activityMainBinding.btnPapel.setOnClickListener(this);
        activityMainBinding.btnTesoura.setOnClickListener(this);

        activityMainBinding.stmMostraOpcoes.setOnCheckedChangeListener((compoundButton, mostrarOpcoes) -> {
            activityMainBinding.llOpcoes.setVisibility(mostrarOpcoes ? View.VISIBLE : View.GONE);
        });

        activityMainBinding.trioRb.setOnCheckedChangeListener((compoundButton, mostrarOpcoes) -> {
            activityMainBinding.tvJogadaComputador2.setVisibility(mostrarOpcoes ? View.VISIBLE : View.GONE);
            activityMainBinding.ivJogadaComputador2.setVisibility(mostrarOpcoes ? View.VISIBLE : View.GONE);
        });

        activityMainBinding.opcaoRg.setOnCheckedChangeListener((radioGroup, i) -> {

            activityMainBinding.tvResultado.setText("");
            activityMainBinding.ivJogadaComputador1.setImageResource(R.mipmap.pedra);
            activityMainBinding.ivJogadaComputador2.setImageResource(R.mipmap.pedra);

        });


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

        if (activityMainBinding.trioRb.isChecked()) {

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

        if(activityMainBinding.duplaRb.isChecked()) {
            resultadoSb.append(". Você " + status1);
        }
        else {
            resultadoSb.append(". Contra o Computador 1, Você " + status1);

            String status2 = verificaStatus(jogada, jogadaComputador2);

            resultadoSb.append(" Contra o Computador 2, Você " + status2);

            if(status1.equals("Perdeu!") && status2.equals("Perdeu!")) {
                resultadoSb.append(" Resultado Final: Você Perdeu para os Computadores!");
            }
            else if(status1.equals("Empatou!") && status2.equals("Empatou!")) {
                resultadoSb.append(" Resultado Final: Você Empatou com os Computadores!");
            }
            else if(status1.equals("Ganhou!") && status2.equals("Ganhou!")) {
                resultadoSb.append(" Resultado Final: Parabéns Você Ganhou dos Computadores!");
            }
            else if(status1.equals("Ganhou!") && status2.equals("Empatou!") ||
                    status2.equals("Ganhou!") && status1.equals("Empatou!")) {
                resultadoSb.append(" Resultado Final: Parabéns Você Ganhou dos Computadores!");
            }
            else if(status1.equals("Ganhou!") && status2.equals("Perdeu!") ||
                    status2.equals("Ganhou!") && status1.equals("Perdeu!")) {
                resultadoSb.append(" Resultado Final: Você Empateu com dos Computadores!");
            }
            else if(status1.equals("Empatou!") && status2.equals("Perdeu!") ||
                    status2.equals("Empatou!") && status1.equals("Perdeu!")) {
                resultadoSb.append(" Resultado Final: Você Perdeu dos Computadores!");
            }
            else {
                resultadoSb.append(" Resultado Impossível!");
            }
        }

        activityMainBinding.tvResultado.setText(resultadoSb.toString());
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
}

