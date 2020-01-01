# MyDownloadManager
download manager using java


The Download Manager uses http 1.1 and configured only to download from http servers.
 we can get a specified chunk of data in http 1.1 protocol so pause and resume are allowed.
The Download Manager is broken into a few classes for natural separation of functional
components. These are the Download, DownloadsTableModel, ProgressRenderer, and
DownloadManager classes, respectively. The DownloadManager class is responsible for the
GUI interface and makes use of the DownloadsTableModel and ProgressRenderer classes
for displaying the current list of downloads. The Download class represents a “managed”
download and is responsible for performing the actual downloading of a file.


Download class -> Download begins by declaring several static final variables that specify the various constants
used by the class. Next, four instance variables are declared. The url variable holds the Internet
URL for the file being downloaded; the size variable holds the size of the download file in
bytes; the downloaded variable holds the number of bytes that have been downloaded thus
far; and the status variable indicates the download’s current status.

The download( ) method creates a new Thread object, passing it a reference to the invoking
Download instance. As mentioned before, it’s necessary for each download to run
independently. In order for the Download class to act alone, it must execute in its own
thread. Java has excellent built-in support for threads and makes using them a snap. To use
threads, the Download class simply implements the Runnable interface by overriding the
run( ) method. After the download( ) method has instantiated a new Thread instance, passing
its constructor the Runnable Download class, it calls the thread’s start( ) method. Invoking
the start( ) method causes the Runnable instance’s (the Download class’) run( ) method to
be executed.

ProgressRender class -> The ProgressRenderer class is a small utility class that is used to render the current progress
of a download listed in the GUI’s “Downloads” JTable instance.it extends JProgressBar and implements TableCellRenderer.

DownloadsTableModel Class -> The DownloadsTableModel class houses the Download Manager’s list of downloads
and is the backing data source for the GUI’s “Downloads” JTable instance.it extends
AbstractTableModel and implements the Observer interface.

DownloadManager Class ->  The DownloadManager class is responsible for creating and running the Download Manager’s GUI. This class has a main( )method declared, so on execution it will be invoked first. The main( ) method instantiates a
new DownloadManager class instance and then calls its show( ) method, which causes it to
be displayed.

comments are added to code to make it more lucid
