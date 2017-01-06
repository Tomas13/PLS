package ru.startandroid.retrofit.ui;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.Interface.GitHubService;
import ru.startandroid.retrofit.Model.History;
import ru.startandroid.retrofit.Model.LastActions;
import ru.startandroid.retrofit.Model.routes.Routes;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.HistoryRVAdapter;

import static ru.startandroid.retrofit.Const.BASE_URL;
import static ru.startandroid.retrofit.utils.Singleton.getUserClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class LastActionsFragment extends Fragment {


    private RecyclerView rvHistory;
    private TextView tvNoDataHistory;
    private ProgressBar progressHistory;

    public LastActionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View viewRoot = inflater.inflate(R.layout.fragment_last_actions, container, false);

        rvHistory = (RecyclerView) viewRoot.findViewById(R.id.rv_fragment_history);
        tvNoDataHistory = (TextView) viewRoot.findViewById(R.id.tv_no_data_history);
        progressHistory = (ProgressBar) viewRoot.findViewById(R.id.progress_history);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Последние действия");


        progressHistory.setVisibility(View.VISIBLE);
        getHistory();


        return viewRoot;
    }

    private void getHistory() {
        Retrofit retrofitLastActions = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient(Const.Token))
                .build();

        GitHubService gitHubServ = retrofitLastActions.create(GitHubService.class);

        final Call<LastActions> callEdges =
                gitHubServ.getLastActions();


        callEdges.enqueue(new Callback<LastActions>() {
            @Override
            public void onResponse(Call<LastActions> call, Response<LastActions> response) {

                progressHistory.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    if (response.isSuccessful() && response.body().getStatus().equals("success")) {

                        final List<History> lastActionsList = new ArrayList<>();

                        for (int i = 0; i < response.body().getHistory().size(); i++) {

                            lastActionsList.add(response.body().getHistory().get(i));
                        }

                        HistoryRVAdapter historyAdapter = new HistoryRVAdapter(lastActionsList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                        rvHistory.setLayoutManager(mLayoutManager);
                        rvHistory.setAdapter(historyAdapter);


//                } else if (response.body().getStatus().equals("list-empty")) {
                    } else {
                        tvNoDataHistory.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<LastActions> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
