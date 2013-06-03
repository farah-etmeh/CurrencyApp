package com.syrisoft.currencyapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.os.AsyncTask;

public class InternetManager extends AsyncTask<String, Void, String> {

	private List<BasicNameValuePair> header;
	
	public static String getQuery(List<NameValuePair> params)
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;

	    for (NameValuePair pair : params)
	    {
	        if (first)
	            first = false;
	        else
	            result.append("&");

	        try {
				result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
		        result.append("=");
		        result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	    System.err.println("query  "+result.toString());
	    return result.toString();
	}

	
	public InternetManager(CallBack<String> callBack) {
		// TODO Auto-generated constructor stub
		this.callBack = callBack;
	}

	@Override
	protected String doInBackground(String... params) {
		boolean get_post = Boolean.valueOf( params[0]);
		if (get_post) {
			this.htmlText = executeGet(params[1]);
		} else {
			this.htmlText = excutePost(params[1], params[2], null);
		}
		
		return this.htmlText;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (this.callBack != null) {
			if (this.htmlText != null) {
				if (!this.htmlText.equals(error)) {
					this.callBack.onFinished(true, this.htmlText, null);
				}
				else {
					this.callBack.onFinished(false, this.htmlText, null);
				}
			}
			else {
				this.callBack.onFinished(false, this.htmlText, null);
			}
		}
		
	}
	
	public byte[] image;
	public List<NameValuePair> params;
	public String excutePost(String targetURL, String urlParameters, List<NameValuePair> headerParameter)
	{
		URL url;
		HttpURLConnection connection = null;  
		try {
			//Create connection
			url = new URL(targetURL);

			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//			connection.setRequestProperty("Content-Type", "multipart/form-data");
			
			if (header != null) {
				for (BasicNameValuePair pair : header) {
					connection.addRequestProperty(pair.getName(), pair.getValue());
				}
			}
			
			
			OutputStream out = connection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
			if (params != null) {
				for (NameValuePair pair : params) {
					writer.write(pair.getName());
					writer.write('=');
					writer.write(pair.getValue());
				}	
			}
					
			writer.write(urlParameters);
			writer.close();
			out.close();
			
			
			
//			DataOutputStream dos = new DataOutputStream(out);
//			dos.writeChars(URLEncoder.encode("ImageFile", "UTF-8"));
//			dos.writeChar('=');
//			dos.write(image);
//			dos.close();
//			out.close();
			connection.connect();
			//Get Response	
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer(); 
			while((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		} finally {

			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}
	
	private String executeGet(String url_string) {
		String result = "";
		try {
			URL url = new URL(url_string);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			if (header != null) {
				for (BasicNameValuePair pair : header) {
					con.addRequestProperty(pair.getName(), pair.getValue());
				}
			}

			result = readStream(con.getInputStream());
			con.disconnect();
		}
		catch (Exception e) {
			e.printStackTrace();
			result = error;
		}
		return result;
	}

	private String readStream(InputStream in) {
		  BufferedReader reader = null;
		  String result = "";
		  try {
		    reader = new BufferedReader(new InputStreamReader(in));
		    String line = "";
		    while ((line = reader.readLine()) != null) {
		    	result = result+ line;
		    }
		  } 
		  catch (IOException e) {
		    e.printStackTrace();
		  } 
		  finally {
		    if (reader != null) {
		      try {
		        reader.close();
		      } catch (IOException e) {
		        e.printStackTrace();
		        }
		    }
		  }
		  return result;
	}

	
	public List<BasicNameValuePair> getHeader() {
		return header;
	}


	public void setHeader(List<BasicNameValuePair> header) {
		this.header = header;
	}

	private CallBack<String> callBack;
	private String htmlText = "0";
	private String error = "0";

}
