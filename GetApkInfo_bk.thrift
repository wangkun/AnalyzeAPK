namespace cpp com.jike.mobile.appsearch.thirft
namespace java com.jike.mobile.appsearch.thirft

//struct ApkSimpleProperty{
//	1: required string packageName;
//	2: required string versionName;
//	3: required string versionCode;
//	4: required list<string> usesPermissonList;
//	5: required list<string> usesFeatureList;
//	6: required i32 minSDK = 0;
//	7: required i32 targetSDK = 0;
//	8: required bool smallScreen = true;
//	9: required bool normalScreen = true;
//	10: required bool largeScreen = true;
//	11: required bool xlargeScreen = true;
//	12: required string signature;
//}

//apk analyze 
struct ApkFullProperty{
	1: required string packageName;                 // the app's package name
	2: required string versionName;                 // the app's versionName
	3: required string versionCode;                 // the app's versionCode
	4: required list<string> usesPermissonList;     // the app's  usesPermissonList
	5: required list<string> usesFeatureList;       // the app's usesFeatureList
	6: required i32 minSDK = 0;                     // the app's package versionName
	7: required i32 targetSDK = 0;                  // the app's package versionName
	8: required bool smallScreen = true;            //this app support smallScreen
	9: required bool normalScreen = true;           //this app support normalScreen
	10: required bool largeScreen = true;           //this app support largeScreen
	11: required bool xlargeScreen = true;          //this app support xlargeScreen
	12: required string signature = "";             //the hash value of apk signature
	13: required binary icon ;                      // icon file from apk file
	14: required map<string,string> appName ;       //mutli language of appName
	15: required list<string> AdsList;              // the Ads contains in apk
	16: required double apkSize = 0;                //the apk size
	17: optional i32 securityLevel = 0;             // security level from apk analyze, not yet finish
}

service GetApkInfo{
	//ApkSimpleProperty getApkSimpleProperty(1:string apkPath);
	ApkFullProperty getApkFullProperty(1:string apkPath);
}