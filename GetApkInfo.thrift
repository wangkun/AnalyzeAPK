namespace cpp com.jike.mobile.appsearch.thirft
namespace java com.jike.mobile.appsearch.thirft

struct ApkSimpleProperty{
	1: required string packageName;
	2: required string versionName;
	3: required string versionCode;
	4: required list<string> usesPermissonList;
	5: required list<string> usesFeatureList;
	6: required i32 minSDK = 0;
	7: required i32 targetSDK = 0;
	8: required bool smallScreen = true;
	9: required bool normalScreen = true;
	10: required bool largeScreen = true;
	11: required bool xlargeScreen = true;
	12: required string signature;
}
struct ApkFullProperty{
	1: required string packageName;
	2: required string versionName;
	3: required string versionCode;
	4: required list<string> usesPermissonList;
	5: required list<string> usesFeatureList;
	6: required i32 minSDK = 0;
	7: required i32 targetSDK = 0;
	8: required bool smallScreen = true;
	9: required bool normalScreen = true;
	10: required bool largeScreen = true;
	11: required bool xlargeScreen = true;
	12: required string signature = "";
	13: required binary icon ;
	14: required map<string,string> appName = null; 
	15: required i32 securityLevel = 0;
	16: required bool isHasAds=false;
}

service GetApkInfo{
	ApkSimpleProperty getApkSimpleProperty(1:string apkPath);
	ApkFullProperty getApkFullProperty(1:string apkPath);
}