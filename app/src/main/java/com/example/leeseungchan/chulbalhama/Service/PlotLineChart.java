package com.example.leeseungchan.chulbalhama.Service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.leeseungchan.chulbalhama.R;
import com.example.leeseungchan.chulbalhama.VO.DatasetVO;
import com.example.leeseungchan.chulbalhama.VO.HabitsVO;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.vision.text.Line;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;


public class PlotLineChart extends AppCompatActivity{
    
    private LineChart mChart;
    private RegressionVO model;
    private DatasetVO dataset;
    private HabitsVO habitsVO;
    
    private ArrayList<Integer> days;
    private ArrayList<Double> scores;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_line_chart);
        Intent intent = getIntent();
        model =(RegressionVO)intent.getSerializableExtra("regressionModel");
        dataset = (DatasetVO)intent.getSerializableExtra("dataset");
        habitsVO = (HabitsVO)intent.getSerializableExtra("habit");
        
        days = dataset.getDays();
        scores = dataset.getScores();
        
        mChart = findViewById(R.id.linechart);
    
        Toolbar toolbarMain = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarMain);
    
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    
        setTitle(habitsVO.getHabitName());
        
        //LineChart lineChart = (LineChart) view.findViewById(R.id.chart);// 승찬씨 이건 Fragment 코드라고 하네여 ㅋ.
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        ArrayList<Entry> estimatedScore = new ArrayList<>(); // 예측점수
        ArrayList<Entry> userScore = new ArrayList<>(); // 유저 점수
    
        float goal= habitsVO.getDue();//DB쿼리로 대체 예정
    
    
    
        for( float i = 0; i < days.get(days.size()-1)+goal + 1; i += 0.02f ){
            estimatedScore.add(new Entry(i,(float)model.estimate(i)));
        }
        for( int i = 0; i < days.size(); i += 1 ) {
            userScore.add(new Entry(i, scores.get(i).floatValue()));
        }
        
        LineDataSet userDataset = new LineDataSet(userScore, "유저 점수");
        LineDataSet estimatedDataset = new LineDataSet(estimatedScore,"기대 점수");
        
        userDataset.setFillAlpha(150);//투명도
        userDataset.setColor(Color.RED);//라인 색상
        userDataset.setCircleColor(Color.RED);
        userDataset.setLineWidth(3f);//두께
        userDataset.setValueTextSize(10f);//데이터 지점 폰트크기
        
        estimatedDataset.setColor(Color.GREEN);
        estimatedDataset.setCircleColor(Color.GREEN);
        
        LimitLine upper_limit = new LimitLine(42f, "최대 점수");
        upper_limit.setLineWidth(4f);
        upper_limit.enableDashedLine(10f, 10f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        upper_limit.setTextSize(15f);
        
        LimitLine habit_gen_limit = new LimitLine(21f, "습관 형성 기준");
        habit_gen_limit.setLineWidth(4f);
        habit_gen_limit.enableDashedLine(10f, 10f, 0f);
        habit_gen_limit.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        habit_gen_limit.setTextSize(15f);
        
        LimitLine habit_goal_limit = new LimitLine(goal, "습관 형성 목표일");
        habit_goal_limit.setLineWidth(4f);
        habit_goal_limit.enableDashedLine(10f, 10f, 0f);
        habit_goal_limit.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        habit_goal_limit.setTextSize(15f);
        habit_goal_limit.setLineColor(Color.MAGENTA);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(upper_limit);
        leftAxis.addLimitLine(habit_gen_limit);
        leftAxis.setAxisMaximum(50f); //플로팅 최댓값
        leftAxis.setAxisMinimum(0f); // 플로팅 최소값
        leftAxis.enableGridDashedLine(10f, 10f, 0);
        leftAxis.setDrawLimitLinesBehindData(true);
        
        mChart.getAxisRight().setEnabled(false);// 오른쪽 legend 제거
        
        //https://github.com/PhilJay/MPAndroidChart/wiki/DataSet-classes-in-detail wiki
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(userDataset);
        dataSets.add(estimatedDataset);
        
        LineData data = new LineData(dataSets);
        
        mChart.setData(data);
        
        Description desc = new Description();
        desc.setText("");
        mChart.setDescription(desc);
        
        
        mChart.getAxisLeft();
        
        XAxis xAxis = mChart.getXAxis();
        //xAxis.setValueFormatter(new MyXAxisValueFormatter(xValues));
        xAxis.setGranularity(1);//x축 시작 (1일차)
        xAxis.setPosition((XAxis.XAxisPosition.BOTTOM));
        xAxis.addLimitLine(habit_goal_limit);
    
        Legend legend = mChart.getLegend();
        legend.setTextSize(18f);
        legend.setXEntrySpace(20f);
        legend.setYEntrySpace(20f);
        
    }
    public void setTitle(String id){
        TextView title = (TextView)findViewById(R.id.toolbar_title);
        title.setText(id);
    }
    
    public class MyXAxisValueFormatter extends ValueFormatter { // 축 포매팅
        private String[] mValues;
        
        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }
        
        @Override
        public String getFormattedValue(float value) {
            return mValues[(int)value];
        }
        
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
