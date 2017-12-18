/* 
 
 Short info:
 
 1) When creating subtask, parent task Fiexed Version(s) should be copied to child task
 2) When parent task is edited, same should happe
 
 To be implemented as a JIRA listener (for Issue Created event and Parent Issue edited )
 
 
 DEC 20117 mika.nokka1@gmail.com
 */
 
 
 
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
 log.setLevel(Level.INFO) // DEBUG INFO
  
 
 log.debug("---------- SetSubTaskVersions started -----------")
 //log.debug("Event:"+event)
 //log.debug("Event: ${event.getEventTypeId()} fired for ${event.issue} and caught by listener")
 //log.debug("ISSUE_CREATED_ID VALUE: ${EventType.ISSUE_CREATED_ID}")
 
 def issue = event.issue as Issue //from Adaptavista examples
 
 if (issue.isSubTask()) {       /* || issue.parentObject.statusObject.name == "Open" */
	   log.debug("This is subtask event")
	   parent=issue.getParentObject()
	   Collection versions=parent.getFixVersions()
	   Collection subtask_versions=issue.getFixVersions()
	   
	   if (!(subtask_versions)){
		   log.info("Subtask(${issue}) has no FixedVersions set by user, going to inherit from the parent ${parent}")
		   
		   if (!(versions)) {
		   		log.info("Parent(${parent}) has no FixedVersions set")
	   		}
	   		else {
				   log.info("Parent(${parent}) FixedVersions:${versions} setting for subtask:${issue}")
		   
				   MutableIssue mutableIssue = issueManager.getIssueObject(event.issue.id)
				   mutableIssue.setFixVersions(versions)
				   issueManager.updateIssue(event.getUser(),mutableIssue, EventDispatchOption.ISSUE_UPDATED, false)   
			   }
	   }
	   else {
		   log.info("Subtask($issue] has fixed versions set. NOT going to overwrite")
	   }
	   
 }
 else {
		log.debug("This is NOT subtask event. Maybe normal task was created")
	   }
 
 
 log.debug("---------- SetSubTaskVersions stopped -----------")