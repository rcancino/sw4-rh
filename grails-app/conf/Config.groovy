// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]
grails.config.locations=["file:${userHome}/.grails/${appName}-config.groovy"] 
// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = 'com.luxsoft.sw4.rh'// change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml'],
	pdf: 			'application/pdf',
	excel: 			'application/vnd.ms-excel',
	ods: 			'application/vnd.oasis.opendocument.spreadsheet',
	rtf: 			'application/rtf',
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']
grails.resources.adhoc.excludes = ['/WEB-INF/**']

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        // filteringCodecForContentType.'text/html' = 'html'
    }
}

 
grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = false
		grails.plugin.springsecurity.debug.useFilter = false
		//sw4.rh.asistencia.rawdata="/Users/rcancino/github/Data/rawdata"
		grails.plugin.springsecurity.active = false
		
		sw4.rh.asistencia.rawdata="Y://NOMIPLUS//RAWDATA"
    }
    production {
        grails.logging.jul.usebridge = false
		sw4.rh.asistencia.rawdata="/mnt/RAWDATA/NOMIPLUS/RAWDATA"
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console appender:
    //
    appenders {
        console name:'stdout', layout:pattern(conversionPattern: '%-5p %c{2} %m%n')
    }

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
		   
	info 'grails.app.controllers'
	info 'grails.plugin.cache'
	
	
	environments{
		
		development{
			info 'grails.app.services.com.luxsoft.sw4'
			info 'grails.app.services.com.luxsoft.rh'
			info 'grails.app.services.com.luxsoft.rh.AsistenciaService'
			info 'grails.app.services.com.luxsoft.rh.IncentivoService'
			info 'grails.app.services.com.luxsoft.rh.NominaService'
			info 'grails.app.services.com.luxsoft.sw4.rh.IncentivoService'
			info 'grails.app.services.com.luxsoft.sw4.rh.InfonavitService'
			info 'grails.app.com.luxsoft.sw4.rh.TiempoExtraService'
			info 'grails.app.services.com.luxsoft.rh.AguinaldoService'
			//info 'com.luxsoft.sw4.rh'
			off 'com.luxsoft.sw4.rh.ProcesadorDeSueldo'
			off 'com.luxsoft.sw4.rh.procesadores.ProcesadorDeChecadas'
			off 'com.luxsoft.sw4.rh.procesadores.AjusteMensualISR'
			off 'com.luxsoft.sw4.rh.ProcesadorSeguroSocial'
			off 'com.luxsoft.sw4.rh.ProcesadorDeISTP'
			off 'com.luxsoft.sw4.rh.ProcesadorDeVacaciones'
			off 'com.luxsoft.sw4.rh.ProcesadorDePrestamosPersonales'
			off 'com.luxsoft.sw4.rh.ProcesadorDeIncentivo'
			off 'com.luxsoft.sw4.rh.ProcesadorDePensionAlimenticia'
			off 'com.luxsoft.sw4.rh.ProcesadorDeOtrasDeducciones'
			info 'com.luxsoft.sw4.rh.procesadores.AjusteIsr'
			
			
			
			
		}
		
		produccion{
			info 'grails.app.controllers'
			error 'grails.app.services.com.luxsoft.sw4'
			error 'grails.plugin.cache'
		}
		
	}
	
}

grails.plugins.twitterbootstrap.fixtaglib = true

grails.databinding.dateFormats= ['dd/MM/yyyy','MMddyyyy', 'yyyy-MM-dd HH:mm:ss.S', "yyyy-MM-dd'T'hh:mm:ss'Z'"]


// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.logout.postOnly = false
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.luxsoft.sec.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.luxsoft.sec.UserRole'
grails.plugin.springsecurity.authority.className = 'com.luxsoft.sec.Role'
//grails.plugin.springsecurity.securityConfigType = "InterceptUrlMap"
//grails.plugin.springsecurity.interceptUrlMap = [
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/':                              ['permitAll'],
	'/index':                         ['permitAll'],
	'/index.gsp':                     ['permitAll'],
	'/**/js/**':                      ['permitAll'],
	'/**/css/**':                     ['permitAll'],
    '/**/less/**':                     ['permitAll'],
	'/**/images/**':                  ['permitAll'],
	'/**/favicon.ico':                ['permitAll'],
    '/login/**':          ['permitAll'],
    '/logout/**':         ['permitAll'],
	'/spring-security-ui/**':		  ['ROLE_ADMIN'],
	'/role/**':			              ['ROLE_ADMIN'],
    '/console/**':                    ['ROLE_ADMIN'],
	'/user/**':                    	  ['ROLE_ADMIN'],
	'/jasper/**':                     ['RH_USER','ROLE_ADMIN']
]
// grails.resources.modules = {
//     'bootswatch' {
//         dependsOn 'bootstrap'
//         resource url:[dir: 'less', file: 'bootswatch.less'], attrs:[rel: "stylesheet/less", type:'css']
//     }

// }


// Added by the Joda-Time plugin:
grails.gorm.default.mapping = {
	"user-type" type: org.jadira.usertype.dateandtime.joda.PersistentDateMidnight, class: org.joda.time.DateMidnight
	"user-type" type: org.jadira.usertype.dateandtime.joda.PersistentDateTime, class: org.joda.time.DateTime
	"user-type" type: org.jadira.usertype.dateandtime.joda.PersistentDateTimeZoneAsString, class: org.joda.time.DateTimeZone
	"user-type" type: org.jadira.usertype.dateandtime.joda.PersistentDurationAsString, class: org.joda.time.Duration
	"user-type" type: org.jadira.usertype.dateandtime.joda.PersistentInstantAsMillisLong, class: org.joda.time.Instant
	"user-type" type: org.jadira.usertype.dateandtime.joda.PersistentInterval, class: org.joda.time.Interval
	"user-type" type: org.jadira.usertype.dateandtime.joda.PersistentLocalDate, class: org.joda.time.LocalDate
	"user-type" type: org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime, class: org.joda.time.LocalDateTime
	"user-type" type: org.jadira.usertype.dateandtime.joda.PersistentLocalTime, class: org.joda.time.LocalTime
	"user-type" type: org.jadira.usertype.dateandtime.joda.PersistentPeriodAsString, class: org.joda.time.Period
	"user-type" type: org.jadira.usertype.dateandtime.joda.PersistentTimeOfDay, class: org.joda.time.TimeOfDay
	"user-type" type: org.jadira.usertype.dateandtime.joda.PersistentYearMonthDay, class: org.joda.time.YearMonthDay
	"user-type" type: org.jadira.usertype.dateandtime.joda.PersistentYears, class: org.joda.time.Years
}
jodatime.format.html5 = true

grails.i18n.cache.seconds=-1