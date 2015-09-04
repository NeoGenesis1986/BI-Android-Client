package com.palliser.neogenesis.bi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.github.mikephil.charting.utils.PercentFormatter;
import com.google.gson.Gson;



public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("BI - " + mTitle);
        actionBar.setLogo(R.mipmap.ic_dashboard);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setIcon(R.mipmap.ic_dashboard);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        Turnover[] turnovers;
        Credit credit;
        Stock[] stocks;
        View rootView;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {

            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            QueryData queryData = new QueryData();
            String TurnoversURL = "http://192.168.102.186/BI/public/turnovers/jsonIndex";
            String CreditsURL = "http://192.168.102.186/BI/public/credits/jsonIndex";
            String StocksURL = "http://192.168.102.186/BI/public/stocks/jsonIndex";
            queryData.execute(TurnoversURL, CreditsURL, StocksURL);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        public void setupTurnoversChart() {
            BarChart barChart = (BarChart) rootView.findViewById(R.id.barChart);
            barChart.setDescription("Description of the chart");
            barChart.setMaxVisibleValueCount(12);
            barChart.setPinchZoom(false);
            barChart.setDrawBarShadow(false);
            barChart.setDrawGridBackground(false);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setSpaceBetweenLabels(0);
            xAxis.setDrawGridLines(false);

            barChart.getAxisLeft().setDrawGridLines(false);

            barChart.animateY(2500);

            barChart.getLegend().setEnabled(false);


            ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

            ArrayList<String> xVals = new ArrayList<String>();

            int size = turnovers.length;
            for (int i = 0; i < size; i++){
                xVals.add(turnovers[i].getDate());
                yVals.add(new BarEntry((int)(turnovers[i].getValue() / 1000.0), i));
            }

            BarDataSet set = new BarDataSet(yVals, "Chiffre d'affaires");
            set.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set.setDrawValues(false);

            ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
            sets.add(set);

            BarData data = new BarData(xVals, sets);

            barChart.setData(data);
            barChart.invalidate();
        }

        public void setupCreditChart() {
            PieChart pieChart = (PieChart) rootView.findViewById(R.id.pieChart);
            pieChart.setUsePercentValues(true);
            pieChart.setDescription("Description of the chart");
            pieChart.setDragDecelerationFrictionCoef(0.95f);
            //Typeface tf = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Light.ttf");
            //pieChart.setCenterTextTypeface(tf);
            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColorTransparent(true);
            pieChart.setTransparentCircleColor(Color.WHITE);
            pieChart.setTransparentCircleAlpha(110);
            pieChart.setHoleRadius(58f);
            pieChart.setTransparentCircleRadius(61f);
            pieChart.setDrawCenterText(true);
            pieChart.setRotationAngle(0);
            pieChart.setRotationEnabled(true);
            pieChart.setCenterText(credit.getDate());

            //set data
            ArrayList<String> xVals = new ArrayList<String>();
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            xVals.add("Suivi < 30");
            yVals.add(new Entry((float)credit.getSuivi00(), 0));
            xVals.add("Suivi > 30");
            yVals.add(new Entry((float)credit.getSuivi30(), 1));
            xVals.add("Suivi > 60");
            yVals.add(new Entry((float) credit.getSuivi60(), 2));
            xVals.add("Suivi > 90");
            yVals.add(new Entry((float) credit.getSuivi90(), 3));

            PieDataSet dataSet = new PieDataSet(yVals, "Crédits en jours");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            ArrayList<Integer> colors = new ArrayList<Integer>();
            for(int c : ColorTemplate.VORDIPLOM_COLORS) {
                colors.add(c);
            }

            dataSet.setColors(colors);

            PieData data = new PieData(xVals, dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.BLACK);
            //data.setValueTypeface(tf);
            pieChart.setData(data);

            pieChart.highlightValues(null);

            pieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);

            Legend l = pieChart.getLegend();
            l.setPosition((Legend.LegendPosition.RIGHT_OF_CHART));
            l.setXEntrySpace((7f));
            l.setYEntrySpace(0f);
            l.setYOffset(0f);

            pieChart.invalidate();
        }

        public void setupLineChart() {
            LineChart lineChart = (LineChart) rootView.findViewById(R.id.lineChart);
            lineChart.setDrawGridBackground(false);
            lineChart.setDescription("Description of the chart");
            lineChart.setNoDataTextDescription("You need to provide data for the chart");
            lineChart.setHighlightEnabled(true);
            lineChart.setTouchEnabled(true);
            lineChart.setDragEnabled(true);
            lineChart.setScaleEnabled(true);
            lineChart.setPinchZoom(true);
            //LimitLine llXAxis = new LimitLine(10f, "Index 10");
            //llXAxis.enableDashedLine(10f, 10f, 0);
            //llXAxis.setLabelPosition(LimitLabelPosition.POS_RIGHT);
            //llXAxis.setTextSize(10f);
            XAxis xAxis = lineChart.getXAxis();
            //xAxis.addLimitLine(llXAxis);
            //LimitLine ll1 = new LimitLine(130f, "Upper Limit");
            //ll1.setLineWidth(4f);
            //ll1.enableDashedLine(10f, 10f, 0);
            //ll1.setLabelPosition(LimitLabelPosition.POS_RIGHT);
            //ll1.setTextSize(10f);
            //LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
            //ll1.setLineWidth(4f);
            //ll1.enableDashedLine(10f, 10f, 0);
            //ll1.setLabelPosition(LimitLabelPosition.POS_RIGHT);
            //ll1.setTextSize(10f);
            YAxis leftAxis = lineChart.getAxisLeft();
            leftAxis.removeAllLimitLines();
            //leftAxis.addLimitLine(ll1);
            //leftAxis.addLimitLine(ll2);
            //leftAxis.setAxisMaxValue(220f);
            //leftAxis.setAxisMinValue(-50f);
            leftAxis.setStartAtZero(true);
            leftAxis.enableGridDashedLine(10f, 10f, 0);
            leftAxis.setDrawLimitLinesBehindData(true);
            lineChart.getAxisRight().setEnabled(false);

            //set data
            ArrayList<String> xVals = new ArrayList<String>();
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            int size = stocks.length;
            for(int i = 0; i < size; i++) {
                xVals.add(stocks[i].getDate());
                yVals.add(new Entry((float)stocks[i].getValue(), i));
            }
            LineDataSet set = new LineDataSet(yVals, "Evolution Stock");
            set.enableDashedLine(10f, 5f, 0f);
            set.setColor(Color.BLACK);
            set.setCircleColor(Color.BLACK);
            set.setLineWidth(1f);
            set.setCircleSize(3f);
            set.setDrawCircleHole(false);
            set.setValueTextSize(9f);
            set.setFillAlpha(65);
            set.setFillColor(Color.BLACK);
            ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
            dataSets.add(set);
            LineData data = new LineData(xVals, dataSets);
            lineChart.setData(data);


            lineChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
            Legend l = lineChart.getLegend();
            l.setForm(Legend.LegendForm.LINE);
            lineChart.invalidate();
        }

        private class QueryData extends AsyncTask<String, Void, String> {

            URL turnoversURL;
            URL creditsURL;
            URL stocksURL;

            private ProgressDialog dialog = new ProgressDialog(PlaceholderFragment.this.getContext());

            @Override
            protected void onPreExecute() {
                dialog.setMessage("Getting data ...");
                dialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                String result = "";
                try {
                    turnoversURL = new URL(params[0]);
                    creditsURL = new URL(params[1]);
                    stocksURL = new URL(params[2]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    result = "fail";
                    return result;
                }
                HttpURLConnection turnoversConnection;
                HttpURLConnection creditsConnection;
                HttpURLConnection stocksConnection;
                try {
                    turnoversConnection = (HttpURLConnection) turnoversURL.openConnection();
                    creditsConnection = (HttpURLConnection) creditsURL.openConnection();
                    stocksConnection = (HttpURLConnection) stocksURL.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                    result = "fail";
                    return result;
                }
                InputStream turnoversInputStream;
                InputStream creditsInputStream;
                InputStream stocksInputStream;
                try {
                    turnoversInputStream = new BufferedInputStream(turnoversConnection.getInputStream());
                    creditsInputStream = new BufferedInputStream(creditsConnection.getInputStream());
                    stocksInputStream = new BufferedInputStream(stocksConnection.getInputStream());
                    byte[] turnoversContent = new byte[1024];
                    byte[] creditsContent = new byte[1024];
                    byte[] stocksContent = new byte[1024];
                    String turnoversResponse = "";
                    String creditsResponse = "";
                    String stocksResponse = "";
                    int turnoversBytesRead = 0;
                    int creditsBytesRead = 0;
                    int stocksBytesRead = 0;
                    while((turnoversBytesRead = turnoversInputStream.read(turnoversContent)) != -1) {
                        turnoversResponse += new String(turnoversContent, 0, turnoversBytesRead);
                    }
                    while((creditsBytesRead = creditsInputStream.read(creditsContent)) != -1) {
                        creditsResponse += new String(creditsContent, 0, creditsBytesRead);
                    }
                    while((stocksBytesRead = stocksInputStream.read(stocksContent)) != -1) {
                        stocksResponse += new String(stocksContent, 0, stocksBytesRead);
                    }
                    Gson gson = new Gson();
                    turnovers = gson.fromJson(turnoversResponse, Turnover[].class);
                    credit = gson.fromJson(creditsResponse, Credit.class);
                    stocks = gson.fromJson(stocksResponse, Stock[].class);
                    result = "success";
                } catch (IOException e) {
                    e.printStackTrace();
                    result = "fail";
                    return result;
                } finally {
                    turnoversConnection.disconnect();
                    creditsConnection.disconnect();
                    stocksConnection.disconnect();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                dialog.dismiss();
                if(result.equals("success")) {
                    setupTurnoversChart();
                    setupCreditChart();
                    setupLineChart();
                } else {
                    Toast.makeText(PlaceholderFragment.this.getContext(), "Error while connecting to server", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
