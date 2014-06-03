package net.mindeos.attmobile.app;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import net.mindeos.attmobile.adapters.ReportAdapter;
import net.mindeos.attmobile.dialogs.CustomDialogBuilder;
import net.mindeos.attmobile.webservice.Att;
import net.mindeos.attmobile.webservice.AttControlCaller;
import net.mindeos.attmobile.pojos.Report;
import net.mindeos.attmobile.pojos.ReportBase;
import net.mindeos.attmobile.pojos.ReportHeader;
import net.mindeos.attmobile.webservice.RestReport;
import net.mindeos.attmobile.pojos.Summary;
import net.mindeos.attmobile.pojos.User;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.Date;


/**
 * The base class for all the report fragments
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public abstract class ReportsFragmentBase extends Fragment {

    /**
     * The complete view
     */
    protected View v;

    private DefaultRenderer mRenderer;

    private ArrayList<Summary> summary;
    private ArrayList<ReportBase> report;
    private ArrayList<Report> modifiedReports;
    /**
     * The selected student.
     */
    protected User selectedstudent;
    /**
     * Indicates whether this is an atthome view or not.
     */
    protected boolean isatthome = false;

    private GraphicalView mChartView;
    private LayoutInflater inflater;
    /**
     * The TextView showing the report is not loaded.
     */
    protected TextView tvReportNotLoaded;
    /**
     * The TextView for report title.
     */
    protected TextView tvReportTitle;
    /**
     * The TextView for report subtitle.
     */
    protected TextView tvReportSubtitle;
    private TextView tvReportFromDate;
    private TextView tvReportToDate;
    private ImageView imgReportPicture;

    /**
     * The Layout showing all the user data.
     */
    protected LinearLayout lyReportsUserData;
    /**
     * The Layout showing reports summary.
     */
    protected LinearLayout lyReportsSummary;

    private ListView lvReportDetail;

    /**
     * The number of headers in the detail.
     */
    protected int headerscount = 0;
    /**
     * The number of items in the detail.
     */
    protected int itemscount = 0;

    private TabHost tabReports;

    /**
     * The start date.
     */
    protected Date startdate = null;
    /**
     * The end date.
     */
    protected Date enddate = null;

    private boolean haschanged = false;

    /**
     * Instantiates a new Reports fragment base.
     */
    public ReportsFragmentBase() {

    }

    /**
     * Prepares fragment data.
     */
    protected abstract void prepareFragmentData();



    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = inf;
        v = inflater.inflate(R.layout.fragment_reports, container, false);

        //Initalize storage structures
        report = new ArrayList<ReportBase>();
        modifiedReports = new ArrayList<Report>();

        prepareFragmentData();


        //Prepare tabs
        tabReports = (TabHost) v.findViewById(R.id.tabReports);

        tabReports.setup();
        TabHost.TabSpec tabSpec = tabReports.newTabSpec("summary");
        tabSpec.setContent(R.id.tabSummary);
        tabSpec.setIndicator(getTabIndicator(tabReports.getContext(), R.string.report_summary));
        tabReports.addTab(tabSpec);

        TabHost.TabSpec tabSpec2 = tabReports.newTabSpec("detail");
        tabSpec2.setContent(R.id.tabDetail);
        tabSpec2.setIndicator(getTabIndicator(tabReports.getContext(), R.string.report_detail));
        tabReports.addTab(tabSpec2);

        tabReports.setVisibility(View.GONE);


        //Prepare all controls
        lyReportsUserData = (LinearLayout) v.findViewById(R.id.lyReportsUserData);
        lyReportsSummary = (LinearLayout) v.findViewById(R.id.lyReportsSummary);
        tvReportNotLoaded = (TextView) v.findViewById(R.id.tvReportNotLoaded);

        lyReportsUserData.setVisibility(View.GONE);
        lyReportsSummary.setVisibility(View.GONE);
        tvReportNotLoaded.setVisibility(View.VISIBLE);

        tvReportTitle = (TextView) v.findViewById(R.id.tvReportTitle);
        tvReportSubtitle = (TextView) v.findViewById(R.id.tvReportSubtitle);
        tvReportFromDate = (TextView) v.findViewById(R.id.tvReportFromDate);
        tvReportToDate = (TextView) v.findViewById(R.id.tvReportToDate);
        imgReportPicture = (ImageView) v.findViewById(R.id.imgReportPicture);

        lvReportDetail = (ListView) v.findViewById(R.id.lvReportDetail);
        lvReportDetail.setAdapter(new ReportAdapter(getActivity(), report, isatthome));

        //Report list events
        lvReportDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReportAdapter tda = (ReportAdapter)parent.getAdapter();
                if (tda.getItem(position).getClass() == Report.class) {
                    Report r = (Report) tda.getItem(position);

                    if (!haschanged) {
                        AttControlBase act = (AttControlBase) getActivity();
                        act.changeMenuItem(R.id.reports_save, true);
                        haschanged = true;
                    }

                    r.setStatusid(Att.getNextStatus(r.getStatusid()));

                    if (!modifiedReports.contains(r)) modifiedReports.add(r);

                    tda.notifyDataSetChanged();
                }
            }
        });

        lvReportDetail.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ReportAdapter tda = (ReportAdapter)parent.getAdapter();
                if (tda.getItem(position).getClass() == Report.class) {
                    Report r = (Report) tda.getItem(position);

                    showRemarksDialog(getActivity(), r);

                }
                return true;
            }
        });

        //Prepare graphic size
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        lyReportsSummary.getLayoutParams().height = size.x * 80 / 100;

        return v;
    }


    /**
     * Shows the reports dialog.
     *
     * @param ctx the context
     */
    protected abstract void showReportsDialog(Context ctx);

    /**
     * Refreshes the interface after a data change.
     */
    protected void refreshInterface() {
        if (selectedstudent != null) {
            tvReportTitle.setText(selectedstudent.getFirstname()+" "+selectedstudent.getLastname());

            if (selectedstudent.getPic1() != Preferences.NOPICTURE) {
                UrlImageViewHelper.setUrlDrawable(imgReportPicture, AttControlCaller.getUserImageURL(selectedstudent.getPic1(), selectedstudent.getPic2()), R.drawable.userplaceholder);
            }
            else {
                UrlImageViewHelper.setUrlDrawable(imgReportPicture, "", R.drawable.userplaceholder);
            }
        }
        else {
            UrlImageViewHelper.setUrlDrawable(imgReportPicture, "", R.drawable.groupplaceholder);
        }

        tvReportFromDate.setText(getString(R.string.fromdate)+": "+Att.sdf.format(startdate));
        tvReportToDate.setText(getString(R.string.todate)+": "+Att.sdf.format(enddate));

        if (haschanged) {
            AttControlBase act = (AttControlBase) getActivity();
            act.changeMenuItem(R.id.reports_save, false);
            haschanged = false;
        }
    }

    private void renderGraph() {
        mRenderer = new DefaultRenderer();

        int[] colors = new int[] {
                Att.getColor(1),
                Att.getColor(2),
                Att.getColor(3),
                Att.getColor(4),
                Att.getColor(5),
                Att.getColor(6),
                Att.getColor(7),
                Att.getColor(8)
        };

        mRenderer.setLabelsTextSize(20);
        mRenderer.setLabelsColor(getResources().getColor(R.color.green4));
        mRenderer.setShowLegend(false);
        mRenderer.setMargins(new int[]{0,0,0,0});
        mRenderer.setZoomButtonsVisible(false);
        mRenderer.setZoomEnabled(false);
        mRenderer.setPanEnabled(false);

        CategorySeries mSeries = new CategorySeries("");

        for (Summary s : summary) {
            String acro = Att.getStatusDescription(s.getStatusid());

            mSeries.add(acro + " (" + s.getStatuscount() + ")", s.getStatuscount());
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(colors[s.getStatusid() - 1 % colors.length]);
            mRenderer.addSeriesRenderer(renderer);
        }

        if (mChartView != null) {
            mChartView.repaint();
        }

        mChartView = ChartFactory.getPieChartView(getActivity(), mSeries, mRenderer);
        mRenderer.setClickEnabled(false);

        LinearLayout layout = (LinearLayout) v.findViewById(R.id.lyReportsChart);
        layout.removeAllViews();
        layout.addView(mChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }

    private View getTabIndicator(Context context, int title) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }

    /**
     * Negative button clicked.
     */
    public void negativeClicked() {
        //Do nothing
    }

    /**
     * Show remarks dialog.
     *
     * @param ctx the context
     * @param r the report information
     */
    protected void showRemarksDialog(Context ctx, final Report r) {

        View promptView = inflater.inflate(R.layout.dialog_remarks, null);
        CustomDialogBuilder alertDialogBuilder = new CustomDialogBuilder(ctx);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setTitle(ctx.getString(R.string.remarks));

        final EditText etRemarks = (EditText)promptView.findViewById(R.id.etRemarksDialogRemarks);
        etRemarks.setText(r.getRemarks());

        // setup a dialog window
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setNegativeButton(R.string.dialog_cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etRemarks.getWindowToken(), 0);
                        dialog.cancel();
                    }
                }
        );
        alertDialogBuilder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Hide soft keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etRemarks.getWindowToken(), 0);

                r.setRemarks(etRemarks.getText().toString());

                if (!modifiedReports.contains(r)) modifiedReports.add(r);

                if (!haschanged) {
                    AttControlBase act = (AttControlBase) getActivity();
                    act.changeMenuItem(R.id.reports_save, true);
                    haschanged = true;
                }

                ((ReportAdapter)lvReportDetail.getAdapter()).notifyDataSetChanged();
            }
        });


        // create an alert dialog
        alertDialogBuilder.show();
    }

    /**
     * Save the changed data.
     */
    public void saveData() {
        try {
            SaveTakeDataTask sst = new SaveTakeDataTask();
            sst.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate the data for the summary.
     *
     * @return the summary information
     */
    protected abstract ArrayList<Summary> generateSummary();

    /**
     * Class for asynchronous operations for getting the summary information.
     */
    protected class GetReportSummaryTask extends AsyncTask<Integer, Integer, ArrayList<Summary>> {

        protected void onPreExecute() {
            v.findViewById(R.id.pbReportLoading).bringToFront();
            v.findViewById(R.id.pbReportLoading).setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(ArrayList<Summary> result) {
            summary = result;

            if (result.size() > 0) {
                tabReports.setVisibility(View.VISIBLE);
                renderGraph();
            }
            else {
                tabReports.setVisibility(View.GONE);
            }

        }

        protected ArrayList<Summary> doInBackground(Integer... params) {
            return generateSummary();
        }
    }


    /**
     * Generate the data for the report.
     *
     * @return the report data
     */
    protected abstract ArrayList<Report> generateReport();

    /**
     * Class for asynchronous operations for getting the report information.
     */
    protected class GetReportTask extends AsyncTask<Integer, Integer, ArrayList<Report>> {

        protected void onPreExecute() {
            v.findViewById(R.id.pbReportLoading).bringToFront();
            v.findViewById(R.id.pbReportLoading).setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(ArrayList<Report> result) {
            report.clear();
            int lastuser = -1;

            headerscount = 0;
            itemscount = result.size();

            if (itemscount > 0) {
                tabReports.setVisibility(View.VISIBLE);
                tvReportNotLoaded.setVisibility(View.GONE);

                for (int i = 0; i < itemscount; i++) {
                    Report r = result.get(i);
                    if (r.getUid() != lastuser) {
                        report.add(new ReportHeader(r.getUid(), r.getUfirstname(), r.getUlastname()));
                        lastuser = r.getUid();
                        headerscount++;
                    }
                    report.add(r);
                }

                ((ReportAdapter)lvReportDetail.getAdapter()).notifyDataSetChanged();
            }
            else {
                tabReports.setVisibility(View.GONE);

                tvReportNotLoaded.setText(getString(R.string.no_reports_this_criteria));
                tvReportNotLoaded.setVisibility(View.VISIBLE);
            }

            v.findViewById(R.id.pbReportLoading).setVisibility(View.INVISIBLE);
        }

        protected ArrayList<Report> doInBackground(Integer... params) {
            return generateReport();
        }
    }


    private class SaveTakeDataTask extends AsyncTask<Void, Integer, Boolean> {
        protected void onPreExecute() {
            v.findViewById(R.id.pbReportLoading).bringToFront();
            v.findViewById(R.id.pbReportLoading).setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(Boolean result) {
            v.findViewById(R.id.pbReportLoading).setVisibility(View.INVISIBLE);

            if (result) Toast.makeText(getActivity(), getString(R.string.attendance_saved), Toast.LENGTH_LONG).show();
            else Toast.makeText(getActivity(), getString(R.string.attendance_notsaved), Toast.LENGTH_LONG).show();

            refreshAfterSave();
        }

        protected Boolean doInBackground(Void... params) {
            try {
                RestReport.saveReportData(modifiedReports);
                return true;
            } catch (Exception e) {
                return false;
            }

        }
    }

    /**
     * Refresh operations after saving the report.
     */
    protected abstract void refreshAfterSave();

}