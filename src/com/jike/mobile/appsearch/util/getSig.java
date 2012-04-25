
package com.jike.mobile.appsearch.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class getSig {
    private static final Object mSync = new Object();
    private static WeakReference<byte[]> mReadBuffer;
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar GetAndroidSig.jar <apk/jar>");
            System.exit(-1);
        }
        String mArchiveSourcePath = args[0];
        WeakReference<byte[]> readBufferRef;

        byte[] readBuffer = (byte[])null;
        synchronized (mSync) {
            readBufferRef = mReadBuffer;
            if (readBufferRef != null) {
                mReadBuffer = null;
                readBuffer = readBufferRef.get();
            }
            if (readBuffer == null) {
                readBuffer = new byte[8192];
                readBufferRef = new WeakReference<byte[]>(readBuffer);
            }
        }
        try {
            JarFile jarFile = new JarFile(mArchiveSourcePath);
            Certificate[] certs = (Certificate[])null;

            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry je = (JarEntry)entries.nextElement();
                if (je.isDirectory()) {
                    continue;
                }
                if (je.getName().startsWith("META-INF/")) {
                    continue;
                }
                Certificate[] localCerts = loadCertificates(jarFile, je, readBuffer);

                if (localCerts == null) {
                    System.err.println("Package has no certificates at entry " + je.getName()
                            + "; ignoring!");
                    jarFile.close();
                    return;
                }
                if (certs == null) {
                    certs = localCerts;
                } else {
                    for (int i = 0; i < certs.length; ++i) {
                        boolean found = false;
                        for (int j = 0; j < localCerts.length; ++j) {
                            if ((certs[i] == null) || (!(certs[i].equals(localCerts[j]))))
                                continue;
                            found = true;
                            break;
                        }

                        if ((found) && (certs.length == localCerts.length))
                            continue;
                        System.err.println("Package has mismatched certificates at entry "
                                + je.getName() + "; ignoring!");
                        jarFile.close();
                        return;
                    }
                }

            }

            jarFile.close();

            synchronized (mSync) {
                mReadBuffer = readBufferRef;
            }

            String[] certStrings = new String[certs.length];

            if ((certs != null) && (certs.length > 0)) {
                int N = certs.length;

                for (int i = 0; i < N; ++i) {
                    String charSig = new String(toChars(certs[i].getEncoded()));

                    certStrings[i] = charSig;
                }

                Arrays.sort(certStrings);

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < certStrings.length; ++i)
                    sb.append(certStrings[i] + "\n");
                System.out.print(sb.toString());
                return;
            }

            System.err.println("Package has no certificates; ignoring!");
            return;
        } catch (CertificateEncodingException ex) {
            Logger.getLogger(getSig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            System.err.println("Exception reading " + mArchiveSourcePath + "\n" + e);
            return;
        } catch (RuntimeException e) {
            System.err.println("Exception reading " + mArchiveSourcePath + "\n" + e);
            return;
        } catch (Exception e) {
            System.err.println("Exception occured \n" + e);
            return;
        }
    }

    public static String getSignatureString(final String apkPathString) {
        String mString = null;

        String mArchiveSourcePath = apkPathString;
        WeakReference<byte[]> readBufferRef;

        byte[] readBuffer = (byte[])null;
        synchronized (mSync) {
            readBufferRef = mReadBuffer;
            if (readBufferRef != null) {
                mReadBuffer = null;
                readBuffer = readBufferRef.get();
            }
            if (readBuffer == null) {
                readBuffer = new byte[8192];
                readBufferRef = new WeakReference<byte[]>(readBuffer);
            }
        }
        try {
            JarFile jarFile = new JarFile(mArchiveSourcePath);
            Certificate[] certs = (Certificate[])null;

            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry je = (JarEntry)entries.nextElement();
                if (je.isDirectory()) {
                    continue;
                }
                if (je.getName().startsWith("META-INF/")) {
                    continue;
                }
                Certificate[] localCerts = loadCertificates(jarFile, je, readBuffer);

                if (localCerts == null) {
                    System.err.println("Package has no certificates at entry " + je.getName()
                            + "; ignoring!");
                    jarFile.close();
                    return mString;
                }
                if (certs == null) {
                    certs = localCerts;
                } else {
                    for (int i = 0; i < certs.length; ++i) {
                        boolean found = false;
                        for (int j = 0; j < localCerts.length; ++j) {
                            if ((certs[i] == null) || (!(certs[i].equals(localCerts[j]))))
                                continue;
                            found = true;
                            break;
                        }

                        if ((found) && (certs.length == localCerts.length))
                            continue;
                        System.err.println("Package has mismatched certificates at entry "
                                + je.getName() + "; ignoring!");
                        jarFile.close();
                        return mString;
                    }
                }

            }

            jarFile.close();

            synchronized (mSync) {
                mReadBuffer = readBufferRef;
            }

            String[] certStrings = new String[certs.length];

            if ((certs != null) && (certs.length > 0)) {
                int N = certs.length;

                for (int i = 0; i < N; ++i) {
                    String charSig = new String(toChars(certs[i].getEncoded()));

                    certStrings[i] = charSig;
                }

                Arrays.sort(certStrings);

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < certStrings.length; ++i)
                    sb.append(certStrings[i] + "\n");
                // System.out.print(sb.toString());
                mString = sb.toString();
                return mString;
            }

            System.err.println("Package has no certificates; ignoring!");
            return mString;
        } catch (CertificateEncodingException ex) {
            Logger.getLogger(getSig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            System.err.println("Exception reading " + mArchiveSourcePath + "\n" + e);
            return mString;
        } catch (RuntimeException e) {
            System.err.println("Exception reading " + mArchiveSourcePath + "\n" + e);
            return mString;
        } catch (Exception e) {
            System.err.println("Exception occured \n" + e);
            return mString;
        }

        return mString;
    }

    private static char[] toChars(byte[] mSignature) {
        byte[] sig = mSignature;
        int N = sig.length;
        int N2 = N * 2;
        char[] text = new char[N2];

        for (int j = 0; j < N; ++j) {
            byte v = sig[j];
            int d = v >> 4 & 0xF;
            text[(j * 2)] = (char)((d >= 10) ? 97 + d - 10 : 48 + d);
            d = v & 0xF;
            text[(j * 2 + 1)] = (char)((d >= 10) ? 97 + d - 10 : 48 + d);
        }

        return text;
    }

    private static Certificate[] loadCertificates(JarFile jarFile, JarEntry je, byte[] readBuffer) {
        try {
            InputStream is = jarFile.getInputStream(je);
            while (is.read(readBuffer, 0, readBuffer.length) != -1)
                ;
            is.close();

            return ((je != null) ? je.getCertificates() : null);
        } catch (IOException e) {
            System.err.println("Exception reading " + je.getName() + " in " + jarFile.getName()
                    + ": " + e);
        }
        return null;
    }
}
