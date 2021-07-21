# Add any ProGuard configurations specific to this
# extension here.

-keep public class com.sumit1334.trycatch.TryCatch {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/sumit1334/trycatch/repack'
-flattenpackagehierarchy
-dontpreverify
