/* 
 
 Short info:
 
 1) When creating subtask, parent task Fiexed Version(s) should be copied to child task
 2) When parent task is edited, same should happe
 
 To be implemented as a JIRA listener (for Issue Created event and XXXX )
 
 
 DEC 20117 mika.nokka1@gmail.com
 */
 
 
 // CONFIGURATIONS
 //def SIMULATION=1 // 0=normal usage 1=simulation, only system log comments made
 // END OF CONFIGURATIONS
 
 
 import com.atlassian.jira.component.ComponentAccessor
 import com.atlassian.jira.issue.Issue
 import com.atlassian.jira.issue.MutableIssue
 import com.atlassian.jira.issue.customfields.option.LazyLoadedOption
 import org.apache.log4j.Logger
 import org.apache.log4j.Level
 import com.atlassian.jira.issue.link.IssueLinkTypeManager
 import com.atlassian.jira.issue.CustomFieldManager
 import com.atlassian.jira.issue.fields.CustomField
 
 
 import com.atlassian.jira.issue.history.ChangeItemBean
 import com.atlassian.jira.event.project.VersionReleaseEvent
 import com.atlassian.jira.issue.IssueFieldConstants // for DUE_DATE
 import com.atlassian.jira.issue.fields.CustomField
 import java.sql.Timestamp
 import com.atlassian.jira.issue.ModifiedValue
 import com.atlassian.jira.issue.util.DefaultIssueChangeHolder
 import com.atlassian.jira.issue.Issue
 
 import com.atlassian.jira.event.type.EventType
 import com.atlassian.jira.user.ApplicationUser
 import com.atlassian.jira.event.type.EventDispatchOption
 import java.util.ArrayList
 import java.util.Collection
 import com.atlassian.jira.issue.util.DefaultIssueChangeHolder
 
 def changeManager = ComponentAccessor.getChangeHistoryManager();
 def issueManager = ComponentAccessor.getIssueManager()
 
 // set logging to Jira log
 def log = Logger.getLogger("SetSubTaskVersions") // change for customer system
 log.setLevel(Level.DEBUG)
  
 
 log.debug("---------- SetSubTaskVersions started -----------")
 log.debug("Event:"+event)
 log.debug("Event: ${event.getEventTypeId()} fired for ${event.issue} and caught by listener")
 //log.debug("ISSUE_CREATED_ID VALUE: ${EventType.ISSUE_CREATED_ID}")
 
 def issue = event.issue as Issue //from Adaptavista examples
 
 if (issue.isSubTask()) {       /* || issue.parentObject.statusObject.name == "Open" */
	   log.debug("This is subtask event")
	   parent=issue.getParentObject()
	   log.debug("Parent:${parent} ")
	   summary=parent.getSummary()
	   log.debug("Parent Summary:${summary} --> subtask:${issue}")
	   Collection versions=parent.getFixVersions()
	  // Collection<Version> versions = new ArrayList<Version>();
	  // fixVersions = fix_Issue.getFixVersions()
	   
	   if (versions==null) {
		   log.debug("null")
	   }
	   else {
		   log.debug("NOT null")
		   log.debug("Parent FixedVersions:${versions}")
		   
		   //issue.setFixVersions([versions])
		   //issue.store() // needed to store changes
		   //ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
		   

		   //issueManager.updateIssue(event.getUser(), issue, EventDispatchOption.ISSUE_CREATED, false)
		   //reIndex(issue)
		   
		   // ei kaadu, ei vahihda fixed versionia
		   //MutableIssue mutableIssue = issue
		   //mutableIssue.setFixVersions([versions])
		   //mutableIssue.store()
		   //issue.setFixVersions([versions])
		   //issue.store()
		   
		   //tämä failaa eventissä
		   MutableIssue mutableIssue = issueManager.getIssueObject(event.issue.id)
		   mutableIssue.setFixVersions(versions)
		   issueManager.updateIssue(event.getUser(),mutableIssue, EventDispatchOption.ISSUE_UPDATED, false)
		   
		   // failaa eventissä
		   //issue.setFixVersions( ["TestiReleaseDev2017"] )
		   //issue.store()
		   //issueManager.updateIssue(event.getUser(),issue, EventDispatchOption.ISSUE_UPDATED, false)
		   
		   
		 //  CustomField subfield = customFieldManager.getCustomFieldObjectByName('Fix Version/s')
		 //  def changeHolder = new DefaultIssueChangeHolder()
		 // subfield.updateValue(null, isue, new ModifiedValue(ChildCustomDueDate,tempdate),changeHolder)
		   
		   
		   
	   }
	   

 }
 else {
		log.debug("This is NOT subtask event. Maybe normal task was created")
	   }
 
 
 log.debug("---------- SetSubTaskVersions stopped -----------")