;--------------------------------

; The name of the installer
Name "iMyAppsClient"

; The file to write
OutFile "iMyAppsClientSetup.exe"

; The default installation directory
InstallDir $PROGRAMFILES\iMyAppsClient

; Registry key to check for directory (so if you install again, it will 
; overwrite the old one automatically)
InstallDirRegKey HKLM "Software\iMyAppsClient" "Install_Dir"

; Request application privileges for Windows Vista
RequestExecutionLevel admin

;--------------------------------

; Pages

Page components
Page directory
Page instfiles

; Load language
LoadLanguageFile "${NSISDIR}\Contrib\Language files\English.nlf"
LoadLanguageFile "${NSISDIR}\Contrib\Language files\TradChinese.nlf"
LoadLanguageFile "${NSISDIR}\Contrib\Language files\SimpChinese.nlf"

UninstPage uninstConfirm
UninstPage instfiles

; Define Wording
LangString StartMenuShortcuts ${LANG_ENGLISH} "Start Menu Shortcuts"
LangString StartMenuShortcuts ${LANG_TRADCHINESE} "在開始程式集建立捷徑"
LangString StartMenuShortcuts ${LANG_SIMPCHINESE} "Start Menu Shortcuts"

LangString AutoStart ${LANG_ENGLISH} "Auto sync at startup"
LangString AutoStart ${LANG_TRADCHINESE} "系統啟動時自動同步"
LangString AutoStart ${LANG_SIMPCHINESE} "Auto sync at startup"

;--------------------------------

; The stuff to install
Section "iMyAppsClient (required)"

  SectionIn RO
  
  ; Set output path to the installation directory.
  SetOutPath $INSTDIR
  
  ; Put file there
  File "D:\MyProject\iMyApps\output\iMyAppsClient.exe"
  File "icon.ico"
  File "icon16.jpg"
  
  ; Write the installation path into the registry
  WriteRegStr HKLM SOFTWARE\iMyAppsClient "Install_Dir" "$INSTDIR"
  
  ; Write the uninstall keys for Windows
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\iMyAppsClient" "DisplayName" "iMyAppsClient"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\iMyAppsClient" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\iMyAppsClient" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\iMyAppsClient" "NoRepair" 1
  WriteUninstaller "uninstall.exe"
  
SectionEnd

; Optional section (can be disabled by the user)
;Section "Start Menu Shortcuts"
Section $(StartMenuShortcuts)

  CreateDirectory "$SMPROGRAMS\iMyAppsClient"
  CreateShortCut "$SMPROGRAMS\iMyAppsClient\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0
  CreateShortCut "$SMPROGRAMS\iMyAppsClient\iMyAppsClient.lnk" "$INSTDIR\iMyAppsClient.exe"
  CreateShortCut "$SMPROGRAMS\iMyAppsClient\QuickSync.lnk" "$INSTDIR\iMyAppsClient.exe" "-k -o"
  
SectionEnd

;--------------------------------
Section $(AutoStart)

  CreateShortCut "$SMSTARTUP\iMyAppsClient.lnk" "$INSTDIR\iMyAppsClient.exe" "-k -o"
  
SectionEnd  

;--------------------------------

; Uninstaller

Section "Uninstall"
  
  ; Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\iMyAppsClient"
  DeleteRegKey HKLM SOFTWARE\iMyAppsClient

  ; Remove files and uninstaller
  Delete $INSTDIR\iMyAppsClient.exe
  Delete $INSTDIR\uninstall.exe

  ; Remove shortcuts, if any
  Delete "$SMPROGRAMS\iMyAppsClient\*.*"

  ; Remove directories used
  RMDir "$SMPROGRAMS\iMyAppsClient"
  RMDir "$INSTDIR"

SectionEnd
