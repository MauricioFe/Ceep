package com.example.ceep.ui.recyclerview.helper.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ceep.dao.NotaDAO;
import com.example.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

public class NotaItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private ListaNotasAdapter adapter;

    public NotaItemTouchHelperCallback(ListaNotasAdapter adapter) {
        this.adapter = adapter;
    }

    //definição para a fazermos animação
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int marcacoesDeDeslize = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int marcacoesDeArrastar = ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(marcacoesDeArrastar, marcacoesDeDeslize);
    }

    //quando o elemento for arrastado ele vai ser acionado
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int posicaoInicial = viewHolder.getAdapterPosition();
        int posicaoFinal = target.getAdapterPosition();
        new NotaDAO().troca(posicaoInicial, posicaoFinal);
        adapter.troca(posicaoInicial, posicaoFinal);
        return true;
    }

    //quando o movimento for de deslizar esse método vai ser acionado
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        new NotaDAO().remove(position);
        adapter.remove(position);
    }
}
