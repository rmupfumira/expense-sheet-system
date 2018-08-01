package expense.sheet.system

import grails.config.Config
import grails.converters.JSON
import grails.core.support.GrailsConfigurationAware
import grails.gorm.transactions.Transactional
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse


class CurrencyConverterService implements GrailsConfigurationAware{

    String fixerioUrl
    String access_key
    String from
    String to

    def convertRandToUSD(Double zarAmount) {

        RestBuilder rest = new RestBuilder()
        String url = "${fixerioUrl}"
        Map params = [access_key: access_key, from: from, to: to, amount: zarAmount]

        def queryString = params.collect {k,v -> "$k=$v" }.join('&')

        url += queryString

        RestResponse restResponse = rest.get(url){
            urlVariables params
        }

        if( restResponse.statusCode.value() == 200 && restResponse.json ) {
            def slurper = new groovy.json.JsonSlurper()
            def jsonResultObject = slurper.parseText(restResponse.json.toString())
            return jsonResultObject.result;
        }

        return 0.0

    }

    @Override
    void setConfiguration(Config co) {
        fixerioUrl = co.getProperty('fixerio.url', String, 'https://data.fixer.io/api/convert?')
        access_key = co.getProperty('fixerio.access_key', String, '494da7fad0b3e05b09a7929ec4c0ae07')
        from = co.getProperty('fixerio.from', String, 'ZAR')
        to = co.getProperty('fixerio.to', String, 'USD')
    }
}
