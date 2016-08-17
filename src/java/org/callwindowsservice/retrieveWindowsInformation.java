/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.callwindowsservice;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author migue
 */
@WebService(serviceName = "retrieveWindowsInformation")
public class retrieveWindowsInformation {

    /**
     * Retrieves CPU Usage Per Core
     */
    @WebMethod(operationName = "retrieveCPUUsage")
    public String retrieveCPUUsage() {
        PowerShellResponse response = PowerShell.executeSingleCommand("Get-counter -counter '\\Processor(*)\\% Processor Time'");

        return response.getCommandOutput();
    }

    /**
     * Retrieves Amount of Memory Usage or Memory Left
     */
    
    @WebMethod(operationName = "retrieveMemoryUsage")
    public String retrieveMemoryUsage() {
        PowerShellResponse response = PowerShell.executeSingleCommand("gwmi win32_operatingsystem | % {$_.totalVisibleMemorySize / 1024 / 1024; $_.freephysicalmemory / 1024 / 1024}");
        return response.getCommandOutput();
    }

    /**
     * Retrieve Amount of Disk Space Left
     */
    
    @WebMethod(operationName = "retrieveFreeDiskSpace")
    public String retrieveFreeDiskSpace() {
         PowerShellResponse response = PowerShell.executeSingleCommand("Get-WMIObject Win32_LogicalDisk | % {$_.deviceid;$_.size / 1GB; $_.freespace / 1GB}");
        return response.getCommandOutput();
    }

    /**
     * Retrieve List of Running Processes
     */
    @WebMethod(operationName = "retrieveRunningProcesses")
    public String retrieveRunningProcesses() {
        PowerShellResponse response = PowerShell.executeSingleCommand("get-process");
        return response.getCommandOutput();
    }

    /**
     * Start Web Services NOTE NOT YET FINALIZED
     */
    @WebMethod(operationName = "startWebService")
    public String startWebService(@WebParam(name = "serviceName") String serviceName) throws IOException {
        PowerShellResponse response = PowerShell.executeSingleCommand("Start-Process powershell.exe -windowstyle hidden -Verb Runas -ArgumentList '-command \"net start " +  serviceName + "\"'");
        return response.getCommandOutput();
    }
    


    /**
     * Web service operation
     */
    @WebMethod(operationName = "retrieveNumberofCores")
    public String retrieveNumberofCores() {
        PowerShellResponse response = PowerShell.executeSingleCommand("gwmi win32_processor | % numberofcores");
        return response.getCommandOutput();
    }

    /**
     * Web service operation
     * @return 
     */
    @WebMethod(operationName = "getWebServiceStatus")
    public String getWebServiceStatus() {
        PowerShellResponse response = PowerShell.executeSingleCommand("Get-Service | Where-Object {$_.name -eq \"MySQL57\" -or $_.name -eq \"Tomcat8\"}");
        return response.getCommandOutput();
    }

    /**
     * Web service operation
     * @param serviceName
     * @return 
     */
    @WebMethod(operationName = "stopWebService")
    public String stopWebService(@WebParam(name = "serviceName") String serviceName) {
         PowerShellResponse response = PowerShell.executeSingleCommand("Start-Process powershell.exe -windowstyle hidden -Verb Runas -ArgumentList '-command \"net stop " +  serviceName + "\"'");
        return response.getCommandOutput();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retrieveDatabases")
    public String retrieveDatabases() throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec("mysql -u root -pomegaman123 -e \"show databases\"");
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
         String sqlDump = stdInput.readLine();
         sqlDump = stdInput.readLine();
          String line = sqlDump + " ";
          while(sqlDump != null){
            sqlDump =  stdInput.readLine();
            if(sqlDump != null){
                line += sqlDump + " ";
            }
            
            
        }
        
   
        return line;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "testPath")
    public boolean testPath(@WebParam(name = "directorypath") String directorypath) {
        PowerShellResponse response = PowerShell.executeSingleCommand("Test-Path C:\\Users\\migue\\Documents\\dumps\\" + directorypath);
        if(response.getCommandOutput().contains("False")){
        return false;
        }
        return true;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "createDirectory")
    public String createDirectory(@WebParam(name = "directorypath") String directorypath) {
        PowerShellResponse response = PowerShell.executeSingleCommand("mkdir C:\\Users\\migue\\Documents\\dumps\\" + directorypath);
        return "hello";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "backupDatabase")
    public String backupDatabase(@WebParam(name = "databasename") String databasename) throws IOException, InterruptedException {
        System.out.println("mysqldump -u root -pomegaman123 " + databasename + " > " +  databasename);
         ProcessBuilder pb = new ProcessBuilder("cmd", "/C", "mysqldump -u root -pomegaman123 " + databasename + " > " +  databasename +".sql");
            pb.directory(new File("C://Users//migue//Documents//dumps//"));
            Process p = pb.start();
            p.waitFor();
            return "Sucessfull Export";
    }



}
