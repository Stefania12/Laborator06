package ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.net.Socket;

import ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.general.Constants;
import ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.general.Utilities;

public class FTPServerCommunicationAsyncTask extends AsyncTask<String, String, Void> {

    private TextView welcomeMessageTextView;

    public FTPServerCommunicationAsyncTask(TextView welcomeMessageTextView) {
        this.welcomeMessageTextView = welcomeMessageTextView;
    }

    @Override
    protected Void doInBackground(String... params) {
        Socket socket = null;
        BufferedReader reader = null;
        String line;
        try {
            socket = new Socket(params[0], Constants.FTP_PORT);
            reader = Utilities.getReader(socket);
            line = reader.readLine();
            if (line != null && line.startsWith(Constants.FTP_MULTILINE_START_CODE)) {
                publishProgress(line);
                while ((line = reader.readLine()) != null) {
                    if (line.equals(Constants.FTP_MULTILINE_END_CODE1) || line.startsWith(Constants.FTP_MULTILINE_END_CODE2)) {
                        break;
                    }
                    publishProgress(line);
                }
            }
            socket.close();
        } catch (Exception exception) {
            Log.d(Constants.TAG, exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        welcomeMessageTextView.setText("");
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        welcomeMessageTextView.append(progress[0]+"\n");
    }

    @Override
    protected void onPostExecute(Void result) {}

}
