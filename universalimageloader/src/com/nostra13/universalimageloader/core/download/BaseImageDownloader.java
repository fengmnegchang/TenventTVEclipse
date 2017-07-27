/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.nostra13.universalimageloader.core.download;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ContentLengthInputStream;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.utils.IoUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Provides retrieving of {@link InputStream} of image by URI from network or file system or app resources.<br />
 * {@link URLConnection} is used to retrieve image stream from network.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.0
 */
public class BaseImageDownloader implements ImageDownloader {
	/** {@value} */
	public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5 * 1000; // milliseconds
	/** {@value} */
	public static final int DEFAULT_HTTP_READ_TIMEOUT = 20 * 1000; // milliseconds

	/** {@value} */
	protected static final int BUFFER_SIZE = 32 * 1024; // 32 Kb
	/** {@value} */
	protected static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

	protected static final int MAX_REDIRECT_COUNT = 5;

	protected static final String CONTENT_CONTACTS_URI_PREFIX = "content://com.android.contacts/";

	private static final String ERROR_UNSUPPORTED_SCHEME = "UIL doesn't support scheme(protocol) by default [%s]. " + "You should implement this support yourself (BaseImageDownloader.getStreamFromOtherSource(...))";

	protected final Context context;
	protected final int connectTimeout;
	protected final int readTimeout;

	public BaseImageDownloader(Context context) {
		this(context, DEFAULT_HTTP_CONNECT_TIMEOUT, DEFAULT_HTTP_READ_TIMEOUT);
	}

	public BaseImageDownloader(Context context, int connectTimeout, int readTimeout) {
		this.context = context.getApplicationContext();
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}

	@Override
	public InputStream getStream(String imageUri, Object extra) throws IOException {
		switch (Scheme.ofUri(imageUri)) {
			case HTTP:
			case HTTPS:
				return getStreamFromNetwork(imageUri, extra);
			case FILE:
				return getStreamFromFile(imageUri, extra);
			case CONTENT:
				return getStreamFromContent(imageUri, extra);
			case ASSETS:
				return getStreamFromAssets(imageUri, extra);
			case DRAWABLE:
				return getStreamFromDrawable(imageUri, extra);
			case UNKNOWN:
			default:
				return getStreamFromOtherSource(imageUri, extra);
		}
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located in the network).
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException if some I/O error occurs during network request or if no InputStream could be created for
	 *                     URL.
	 */
	protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {
//		HttpURLConnection conn = createConnection(imageUri, extra);
//		String encodedUrl = Uri.encode(imageUri, ALLOWED_URI_CHARS);
//		HttpURLConnection conn = (HttpURLConnection) new URL(encodedUrl).openConnection();
//		conn.setConnectTimeout(connectTimeout);
//		conn.setReadTimeout(readTimeout);
		
		 URL url = null;
	        try {
	            url = new URL(imageUri);
	        } catch (MalformedURLException e) {
	            Log.e("getStreamFromNetwork", e.getMessage(), e);
	        }
	        HttpURLConnection conn = null;

	        if (Scheme.ofUri(imageUri) == Scheme.HTTPS) {
	            trustAllHosts();
	            HttpsURLConnection https = (HttpsURLConnection) url
	                    .openConnection();
	            https.setHostnameVerifier(DO_NOT_VERIFY);
	            
	            https.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");  
	            https.setRequestProperty("Upgrade-Insecure-Requests", "1");  
	            if(imageUri.contains("img.pximg.com") ){
	            	https.setRequestProperty("Host", "img.pximg.com");
	            	https.setRequestProperty("referer", "img.pximg.com");
	            }else if(imageUri.contains("www.pximg.com")){
	            	https.setRequestProperty("Host", "www.pximg.com");
	            	https.setRequestProperty("referer", "www.pximg.com");
	            }
	            https.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36");  
//	            https.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
	            https.setRequestProperty("Accept-Encoding", ":gzip, deflate, sdch");
	            https.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
	            https.setRequestProperty("Cache-Control", "max-age=0");
	            https.setRequestProperty("Connection", "keep-alive");
	            conn = https;
	            conn.connect();
	        } else {
	        	conn = (HttpURLConnection) url.openConnection();
	        }
	        conn.setConnectTimeout(connectTimeout);
	        conn.setReadTimeout(readTimeout);
		
		int redirectCount = 0;
		while (conn.getResponseCode() / 100 == 3 && redirectCount < MAX_REDIRECT_COUNT) {
			conn = createConnection(conn.getHeaderField("Location"), extra);
			redirectCount++;
		}
		conn.getResponseCode();
		InputStream imageStream;
		try {
			imageStream = conn.getInputStream();
		} catch (IOException e) {
			// Read all data to allow reuse connection (http://bit.ly/1ad35PY)
			IoUtils.readAndCloseStream(conn.getErrorStream());
			throw e;
		}
		if (!shouldBeProcessed(conn)) {
			IoUtils.closeSilently(imageStream);
			throw new IOException("Image request failed with response code " + conn.getResponseCode());
		}

		return new ContentLengthInputStream(new BufferedInputStream(imageStream, BUFFER_SIZE), conn.getContentLength());
	}

	   // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    
	/**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	/**
	 * @param conn Opened request connection (response code is available)
	 * @return <b>true</b> - if data from connection is correct and should be read and processed;
	 *         <b>false</b> - if response contains irrelevant data and shouldn't be processed
	 * @throws IOException
	 */
	protected boolean shouldBeProcessed(HttpURLConnection conn) throws IOException {
		return conn.getResponseCode() == 200;
	}

	/**
	 * Create {@linkplain HttpURLConnection HTTP connection} for incoming URL
	 *
	 * @param url   URL to connect to
	 * @param extra Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *              DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@linkplain HttpURLConnection Connection} for incoming URL. Connection isn't established so it still configurable.
	 * @throws IOException if some I/O error occurs during network request or if no InputStream could be created for
	 *                     URL.
	 */
	protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
		String encodedUrl = Uri.encode(url, ALLOWED_URI_CHARS);
		HttpURLConnection conn = (HttpURLConnection) new URL(encodedUrl).openConnection();
		conn.setConnectTimeout(connectTimeout);
		conn.setReadTimeout(readTimeout);
		return conn;
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located on the local file system or SD card).
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException if some I/O error occurs reading from file system
	 */
	protected InputStream getStreamFromFile(String imageUri, Object extra) throws IOException {
		String filePath = Scheme.FILE.crop(imageUri);
		if (isVideoFileUri(imageUri)) {
			return getVideoThumbnailStream(filePath);
		} else {
			BufferedInputStream imageStream = new BufferedInputStream(new FileInputStream(filePath), BUFFER_SIZE);
			return new ContentLengthInputStream(imageStream, (int) new File(filePath).length());
		}
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	private InputStream getVideoThumbnailStream(String filePath) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			Bitmap bitmap = ThumbnailUtils
					.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
			if (bitmap != null) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 0, bos);
				return new ByteArrayInputStream(bos.toByteArray());
			}
		}
		return null;
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is accessed using {@link ContentResolver}).
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws FileNotFoundException if the provided URI could not be opened
	 */
	@SuppressLint("NewApi") protected InputStream getStreamFromContent(String imageUri, Object extra) throws FileNotFoundException {
		ContentResolver res = context.getContentResolver();

		Uri uri = Uri.parse(imageUri);
		if (isVideoContentUri(uri)) { // video thumbnail
			Long origId = Long.valueOf(uri.getLastPathSegment());
			Bitmap bitmap = MediaStore.Video.Thumbnails
					.getThumbnail(res, origId, MediaStore.Images.Thumbnails.MINI_KIND, null);
			if (bitmap != null) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 0, bos);
				return new ByteArrayInputStream(bos.toByteArray());
			}
		} else if (imageUri.startsWith(CONTENT_CONTACTS_URI_PREFIX)) { // contacts photo
			return getContactPhotoStream(uri);
		}

		return res.openInputStream(uri);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	protected InputStream getContactPhotoStream(Uri uri) {
		ContentResolver res = context.getContentResolver();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return ContactsContract.Contacts.openContactPhotoInputStream(res, uri, true);
		} else {
			return ContactsContract.Contacts.openContactPhotoInputStream(res, uri);
		}
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located in assets of application).
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException if some I/O error occurs file reading
	 */
	protected InputStream getStreamFromAssets(String imageUri, Object extra) throws IOException {
		String filePath = Scheme.ASSETS.crop(imageUri);
		return context.getAssets().open(filePath);
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located in drawable resources of application).
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 */
	protected InputStream getStreamFromDrawable(String imageUri, Object extra) {
		String drawableIdString = Scheme.DRAWABLE.crop(imageUri);
		int drawableId = Integer.parseInt(drawableIdString);
		return context.getResources().openRawResource(drawableId);
	}

	/**
	 * Retrieves {@link InputStream} of image by URI from other source with unsupported scheme. Should be overriden by
	 * successors to implement image downloading from special sources.<br />
	 * This method is called only if image URI has unsupported scheme. Throws {@link UnsupportedOperationException} by
	 * default.
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException                   if some I/O error occurs
	 * @throws UnsupportedOperationException if image URI has unsupported scheme(protocol)
	 */
	protected InputStream getStreamFromOtherSource(String imageUri, Object extra) throws IOException {
		throw new UnsupportedOperationException(String.format(ERROR_UNSUPPORTED_SCHEME, imageUri));
	}

	private boolean isVideoContentUri(Uri uri) {
		String mimeType = context.getContentResolver().getType(uri);
		return mimeType != null && mimeType.startsWith("video/");
	}

	private boolean isVideoFileUri(String uri) {
		String extension = MimeTypeMap.getFileExtensionFromUrl(uri);
		String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		return mimeType != null && mimeType.startsWith("video/");
	}
}
