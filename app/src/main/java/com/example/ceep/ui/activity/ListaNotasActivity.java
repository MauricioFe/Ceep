package com.example.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ceep.R;
import com.example.ceep.dao.NotaDAO;
import com.example.ceep.models.Nota;
import com.example.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import com.example.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;

import java.util.List;

import static com.example.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static com.example.ceep.ui.activity.NotaActivityConstantes.REQUEST_CODE_INSERE_NOTA;
import static com.example.ceep.ui.activity.NotaActivityConstantes.RESULT_CODE_NOTA_CRIADA;

public class ListaNotasActivity extends AppCompatActivity {
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        List<Nota> todasNotas = getTodasNotas();
        configuraRecyclerView(todasNotas);
        configuraBotaoInsereNota();
    }

    private void configuraBotaoInsereNota() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaiParaFormularioNotaActivity();
            }
        });
    }

    private void vaiParaFormularioNotaActivity() {
        //inicializa activity esperando um resultado
        startActivityForResult(new Intent(ListaNotasActivity.this, FormularioNotaActivity.class), REQUEST_CODE_INSERE_NOTA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (isResultNota(requestCode, resultCode, data)) {
            assert data != null;
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            adiciona(notaRecebida);
        }
        if (requestCode == 2 && resultCode == RESULT_CODE_NOTA_CRIADA && hasNota(data)){
            assert data != null;
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            Toast.makeText(this, notaRecebida.getTitulo(), Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void adiciona(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private boolean isResultNota(int requestCode, int resultCode, @Nullable Intent data) {
        return isCodigoRequisicaoInsereNota(requestCode) && isCodigoResultadoNotaCriada(resultCode) && hasNota(data);
    }

    private boolean hasNota(@Nullable Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    private boolean isCodigoResultadoNotaCriada(int resultCode) {
        return resultCode == RESULT_CODE_NOTA_CRIADA;
    }

    private boolean isCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == REQUEST_CODE_INSERE_NOTA;
    }

    private List<Nota> getTodasNotas() {
        NotaDAO dao = new NotaDAO();
        for (int i = 0; i < 10; i++) {
            dao.insere(new Nota("Título " + (i + 1), "Descrição " + (i + 1)));
        }
        return dao.todos();
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(todasNotas, this);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota) {
                Intent intent = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
                intent.putExtra(CHAVE_NOTA, nota);
                startActivityForResult(intent, 2);
            }
        });
    }
}