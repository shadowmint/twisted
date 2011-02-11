package twisted.client.utils;

import java.util.ArrayList;

import com.google.gwt.user.client.Timer;

import twisted.client.ComponentLog;

/** 
 * Helper class for when you have a series of tasks to run.
 * <p>
 * Tasks are run in the order they are added in. 
 * <p>
 * On error, tasks can either fail the entire set or fail only
 * that one task and skip onto the next. Make the task list fail
 * call @see #fail(Throwable) from the onfailure for the callback.
 * <p>
 * Tasks must <i>manually</i> request that the next task be run
 * by invoking the next() call.
 */
public class AsyncTaskList {
  
  /** Set of tasks. */
  private ArrayList<GenericCallback<Void>> tasks = new ArrayList<GenericCallback<Void>>(); 
  
  /** Running tasks. */
  private ArrayList<GenericCallback<Void>> running = new ArrayList<GenericCallback<Void>>(); 
  
  /** Current task. */
  private GenericCallback<Void> task = null;
  
  /** If we've had a failAll call. */
  private Throwable failed = null;
  
  /** Completion timer. */
  private Timer timer = null;
  
  /** Completion timeout. */
  private int timeout;
  
  /** Completion timeout default value. */
  private static int defaultTimeout = 0; 
  
  public AsyncTaskList() {
    timeout = defaultTimeout;
  }
  
  /** 
   * Add a task to run. 
   * <p>
   * @param task A GenericCallback with the task in the onSuccess() handler.
   */
  public void addTask(GenericCallback<Void> task) {
    tasks.add(task);
  }
  
  /** Sets the default timeout. */
  public static void setDefaultTimeout(int timeout) {
    defaultTimeout = timeout;
  }
  
  /** 
   * Set the timeout. 
   * <p>
   * The timeout is invoked if nothing calls next() after the timeout
   * period and there are still tasks in the queue. 
   * <p>
   * To disable this, set the timeout to 0.
   */
  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }
  
  /** Invoked when the timer goes off. */
  public void onTimeout() {
    timer = null;
    failed = new Exception("Timeout waiting " + timeout + "ms for: " + task + ". Did something forget to call AsyncTaskList.next()?");
    ComponentLog.trace(failed.toString());
  }
  
  /** Run all the tasks in the list. */
  @SuppressWarnings("unchecked")
  public void run() {
    
    // Reset state
    failed = null;
    task = null;
    if (timer != null) 
      timer.cancel();
    timer = null;
    
    // Create a timeout timer
    timer = new Timer() {
      public void run() {
        onTimeout();
      }
    };
    if (timeout > 0)
      timer.schedule(timeout);
    
    // Get a clone of the task list so this is repeatable.
    try {
      Object o = tasks.clone();
      running = (ArrayList<GenericCallback<Void>>) o;
    }
    catch(Exception e) {
      failed = e;
    }
    
    runNextTask();
  }
  
  /** Runs the next task in the list. */
  protected void runNextTask() {
    if (running.size() > 0)
      task = running.remove(0);
    else
      task = null;
    if (task != null) {
      if (failed == null)
        CommonEvents.run(task);
      else
        CommonEvents.run(task, failed);
    }
    else {
      if (timer != null)
        timer.cancel();
      timer = null;
    }
  }
  
  /** 
   * Marks all left over tasks to be failed.
   * <p> 
   * No need to call next() after calling this.
   */
  public void fail(Throwable caught) {
    failed = caught;
    next();
  }
  
  /** 
   * Request that the next task get run. 
   * <p>
   * The next task is not immediately, but is deferred until
   * the next callback is invoked.
   */
  public void next() {
    CommonEvents.run(new GenericCallback<Void>() {
      public void onFailure(Throwable caught) {}
      public void onSuccess(Void result) {
        runNextTask();
      }
    });
  }
}
