package com.example.legoportalproject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class ComBluetooth {
    private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    BluetoothAdapter localAdapter;
    BluetoothSocket socket_ev3;
    boolean success=false;
    private boolean btPermission=false;
    private boolean alertReplied=false;

    public void reply(){this.alertReplied = true;}
    public void setBtPermission(boolean btPermission) {
        this.btPermission = btPermission;
    }

    public boolean initBT(){
        localAdapter= BluetoothAdapter.getDefaultAdapter();
        return localAdapter.isEnabled();
    }

    //connect to both NXTs
    public  boolean connectToEV3(String macAdd){
        //get the BluetoothDevice of the EV3
        BluetoothDevice ev3 = localAdapter.getRemoteDevice(macAdd);
        //try to connect to the nxt
        try {
            socket_ev3 = ev3.createRfcommSocketToServiceRecord(UUID.fromString(SPP_UUID));
            socket_ev3.connect();
            success = true;
        } catch (IOException e) {
            Log.d("Bluetooth","Err: Device not found or cannot connect " + macAdd);
            success=false;
        }
        return success;
    }

    public void writeMessage(byte msg) throws InterruptedException {
        BluetoothSocket connSock;
        connSock= socket_ev3;
        if(connSock!=null){
            try {
                OutputStreamWriter out=new OutputStreamWriter(connSock.getOutputStream());
                out.write(msg);
                out.flush();
                Thread.sleep(1000);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            //Error
        }
    }

    public void run(TextView msgEtat, ComBluetooth comBluetooth) {

            try {
                int msg = 0;
                InputStreamReader in = new InputStreamReader(comBluetooth.socket_ev3.getInputStream());
                while (true) {
                    if (in.ready()) {
                        msg = in.read();
                        switch (msg) {
                            case 1:
                                msgEtat.setText("En fermeture");
                                break;
                            case 2:
                                msgEtat.setText("En attente de réponse de l'EV3");
                                break;
                            default:
                                msgEtat.setText("En fermeture ?");
                                break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}