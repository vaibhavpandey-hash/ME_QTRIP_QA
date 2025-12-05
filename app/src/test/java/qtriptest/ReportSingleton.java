package qtriptest;

import com.relevantcodes.extentreports.ExtentReports;
import java.io.File;
import java.sql.Timestamp;

public class ReportSingleton {
    private static ExtentReports report;

    public synchronized static ExtentReports getInstance() {
        if (report == null) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String timestampString = String.valueOf(timestamp.getTime());

            // Construct the report path correctly
            String reportPath = System.getProperty("user.dir") + "/OurExtentReport_" + timestampString + ".html";
            
            // Create the instance of ExtentReports
            report = new ExtentReports(reportPath, true);
            
            // Check if the config file exists before loading
            File configFile = new File(System.getProperty("user.dir") + "/config.xml");
            if (configFile.exists()) {
                report.loadConfig(configFile);
            } else {
                System.out.println("Warning: config.xml file not found, proceeding without loading configuration.");
            }
        }
        return report;
    }
}
