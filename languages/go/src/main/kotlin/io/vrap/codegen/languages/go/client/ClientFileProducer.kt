package io.vrap.codegen.languages.go.client

import io.vrap.codegen.languages.go.*
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.raml.model.modules.Api

class ClientFileProducer constructor(
    val clientConstants: ClientConstants,
    val api: Api,
    @BasePackageName val basePackageName: String
) : FileProducer {

    override fun produceFiles(): List<TemplateFile> {

        return listOf(
            produceClientFile(),
            produceClientApiRoot(api),
            produceClientLoggingFile(),
            produceErrorsFile(),
            produceUtilsFile(),
            produceDateFile()
        )
    }

    fun produceClientFile(): TemplateFile {
        return TemplateFile(
            relativePath = "$basePackageName/client.go",
            content = """|
                |$goGeneratedComment
                |package $basePackageName
                |
                |import (
                |    "context"
                |    "io"
                |    "fmt"
                |    "net/http"
                |    "net/url"
                |    "runtime"
                |    "golang.org/x/oauth2"
                |    "golang.org/x/oauth2/clientcredentials"
                |)
                |
                |// Version identifies the current library version. Should match the git tag
                |const Version = "0.1.0"
                |
                |type Client struct {
                |    httpClient *http.Client
                |    url        *url.URL
                |    logLevel   int
                |    userAgent  string
                |}
                |
                |type ClientConfig struct {
                |    URL         string
                |    Credentials *clientcredentials.Config
                |    LogLevel    int
                |    HTTPClient  *http.Client
                |    UserAgent   string
                |}
                |
                |// NewClient creates a new client based on the provided ClientConfig
                |func NewClient(cfg *ClientConfig) (*Client, error) {
                |
                |    // If a custom httpClient is passed use that
                |    var httpClient *http.Client
                |    if cfg.HTTPClient != nil {
                |        if (cfg.Credentials != nil) {
                |            httpClient = cfg.Credentials.Client(
                |                context.WithValue(oauth2.NoContext, oauth2.HTTPClient, cfg.HTTPClient))
                |        } else {
                |            httpClient = cfg.HTTPClient
                |        }
                |    } else {
                |        httpClient = cfg.Credentials.Client(context.TODO())
                |    }
                |
                |    var userAgent = cfg.UserAgent
                |    if (userAgent == "") {
                |        userAgent = GetUserAgent()
                |    }
                |
                |    url, err := url.Parse(cfg.URL)
                |    if err != nil {
                |        return nil, err
                |    }
                |    client := &Client{
                |        url:        url,
                |        logLevel:   cfg.LogLevel,
                |        httpClient: httpClient,
                |        userAgent:  userAgent,
                |    }
                |
                |    return client, nil
                |}
                |
                |func (c* Client) createEndpoint(p string) (*url.URL, error) {
                |    url, err := url.Parse(p)
                |    if err != nil {
                |        return nil, err
                |    }
                |    return c.url.ResolveReference(url), nil
                |}
                |
                |func (c *Client) head(ctx context.Context, path string, queryParams url.Values, headers http.Header) (*http.Response, error) {
                |    return c.execute(ctx, http.MethodHead, path, queryParams, headers, nil)
                |}
                |
                |func (c *Client) get(ctx context.Context, path string, queryParams url.Values, headers http.Header) (*http.Response, error) {
                |    return c.execute(ctx, http.MethodGet, path, queryParams, headers, nil)
                |}
                |
                |func (c *Client) post(ctx context.Context, path string, queryParams url.Values, headers http.Header, body io.Reader) (*http.Response, error) {
                |    return c.execute(ctx, http.MethodPost, path, queryParams, headers, body)
                |}
                |
                |func (c *Client) put(ctx context.Context, path string, queryParams url.Values, headers http.Header, body io.Reader) (*http.Response, error) {
                |    return c.execute(ctx, http.MethodPut, path, queryParams, headers, body)
                |}
                |
                |func (c *Client) delete(ctx context.Context, path string, queryParams url.Values, headers http.Header, body io.Reader) (*http.Response, error) {
                |    return c.execute(ctx, http.MethodDelete, path, queryParams, headers, body)
                |}
                |
                |func (c *Client) execute(ctx context.Context, method string, path string, params url.Values, headers http.Header, body io.Reader) (*http.Response, error) {
                |    endpoint, err := c.createEndpoint(path)
                |    if (err != nil) {
                |        return nil, err
                |    }
                |
                |    if params != nil {
                |        endpoint.RawQuery = params.Encode()
                |    }
                |
                |    req, err := http.NewRequestWithContext(ctx, method, endpoint.String(), body)
                |    if err != nil {
                |        return nil, fmt.Errorf("creating new request: %w", err)
                |    }
                |
                |    if (headers != nil) {
                |        req.Header = headers
                |    }
                |    req.Header.Set("Accept", "application/json; charset=utf-8")
                |    req.Header.Set("Content-Type", "application/json; charset=utf-8")
                |    req.Header.Set("User-Agent", c.userAgent)
                |
                |    if c.logLevel > 0 {
                |        logRequest(req)
                |    }
                |
                |    resp, err := c.httpClient.Do(req)
                |    if err != nil {
                |        return nil, err
                |    }
                |
                |    if c.logLevel > 0 {
                |        logResponse(resp)
                |    }
                |
                |    return resp, nil
                |}
                |
                |func GetUserAgent() string {
                |   return fmt.Sprintf("commercetools-go-sdk/%s Go/%s (%s; %s)",
                |        Version, runtime.Version(), runtime.GOOS, runtime.GOARCH)
                |}
            """.trimMargin()
        )
    }

    fun produceClientApiRoot(type: Api): TemplateFile {
        return TemplateFile(
            relativePath = "$basePackageName/client_api_root.go",
            content = """|
                |$goGeneratedComment
                |package $basePackageName
                |
                |<${type.subResources("Client")}>
                |
            """.trimMargin().keepIndentation()
        )
    }

    fun produceClientLoggingFile(): TemplateFile {
        return TemplateFile(
            relativePath = "$basePackageName/client_logger.go",
            content = """
                |$goGeneratedComment
                |package $basePackageName
                |
                |import (
                |	"log"
                |	"net/http"
                |	"net/http/httputil"
                |)
                |
                |const logRequestTemplate = `DEBUG:
                |---[ REQUEST ]--------------------------------------------------------
                |%s
                |----------------------------------------------------------------------
                |`
                |
                |const logResponseTemplate = `DEBUG:
                |---[ RESPONSE ]-------------------------------------------------------
                |%s
                |----------------------------------------------------------------------
                |`
                |
                |func logRequest(r *http.Request) {
                |	body, err := httputil.DumpRequestOut(r, true)
                |	if err != nil {
                |		return
                |	}
                |	log.Printf(logRequestTemplate, body)
                |}
                |
                |func logResponse(r *http.Response) {
                |	body, err := httputil.DumpResponse(r, true)
                |	if err != nil {
                |		return
                |	}
                |	log.Printf(logResponseTemplate, body)
                |}
                """.trimMargin()
        )
    }

    fun produceErrorsFile(): TemplateFile {
        return TemplateFile(
            relativePath = "$basePackageName/errors.go",
            content = """|
                |$goGeneratedComment
                |package $basePackageName
                |
                |import (
                |	"fmt"
                |)
                |
                |type GenericRequestError struct {
                |    Content    []byte
                |    StatusCode int
                |}
                |
                |func (e GenericRequestError) Error() string {
                |    return fmt.Sprintf("Request returned status code %d", e.StatusCode)
                |}
            """.trimMargin().keepIndentation()
        )
    }

    fun produceUtilsFile(): TemplateFile {
        return TemplateFile(
            relativePath = "$basePackageName/utils.go",
            content = """|
                |$goGeneratedComment
                |package $basePackageName
                |
                |import (
                |    "bytes"
                |    "encoding/json"
                |    "fmt"
                |    "io"
                |    "reflect"
                |    "time"
                |
                |    mapstructure "github.com/mitchellh/mapstructure"
                |)
                |
                |func serializeInput(input interface{}) (io.Reader, error) {
                |    m, err := json.MarshalIndent(input, "", "  ")
                |    if err != nil {
                |        return nil, fmt.Errorf("unable to serialize content: %w", err)
                |    }
                |    data := bytes.NewReader(m)
                |    return data, nil
                |}
                |
                |func toTimeHookFunc() mapstructure.DecodeHookFunc {
                |    return func(
                |        f reflect.Type,
                |        t reflect.Type,
                |        data interface{}) (interface{}, error) {
                |        if t != reflect.TypeOf(time.Time{}) {
                |            return data, nil
                |        }
                |
                |        switch f.Kind() {
                |        case reflect.String:
                |            return time.Parse(time.RFC3339, data.(string))
                |        case reflect.Float64:
                |            return time.Unix(0, int64(data.(float64))*int64(time.Millisecond)), nil
                |        case reflect.Int64:
                |            return time.Unix(0, data.(int64)*int64(time.Millisecond)), nil
                |        default:
                |            return data, nil
                |        }
                |        // Convert it by parsing
                |    }
                |}
                |
                |func decodeStruct(input interface{}, result interface{}) error {
                |    decoder, err := mapstructure.NewDecoder(&mapstructure.DecoderConfig{
                |        Metadata: nil,
                |        DecodeHook: mapstructure.ComposeDecodeHookFunc(
                |            toTimeHookFunc()),
                |        Result: result,
                |    })
                |    if err != nil {
                |        return err
                |    }
                |
                |    if err := decoder.Decode(input); err != nil {
                |        return err
                |    }
                |    return err
                |}
            """.trimMargin().keepIndentation()
        )
    }

    fun produceDateFile(): TemplateFile {
        return TemplateFile(
            relativePath = "$basePackageName/date.go",
            content = """|
                |$goGeneratedComment
                |package $basePackageName
                |
                |import (
                |    "encoding/json"
                |    "fmt"
                |    "strconv"
                |    "time"
                |)
                |
                |// Date holds date information for Commercetools API format
                |type Date struct {
                |    Year  int
                |    Month time.Month
                |    Day   int
                |}
                |
                |// NewDate initializes a Date struct
                |func NewDate(year int, month time.Month, day int) Date {
                |    return Date{Year: year, Month: month, Day: day}
                |}
                |
                |// MarshalJSON marshals into the commercetools date format
                |func (d *Date) MarshalJSON() ([]byte, error) {
                |    value := fmt.Sprintf("%04d-%02d-%02d", d.Year, d.Month, d.Day)
                |    return []byte(strconv.Quote(value)), nil
                |}
                |
                |// UnmarshalJSON decodes JSON data into a Date struct
                |func (d *Date) UnmarshalJSON(data []byte) error {
                |    var input string
                |    err := json.Unmarshal(data, &input)
                |    if err != nil {
                |        return err
                |    }
                |
                |    value, err := time.Parse("2006-01-02", input)
                |    if err != nil {
                |        return err
                |    }
                |
                |    d.Year = value.Year()
                |    d.Month = value.Month()
                |    d.Day = value.Day()
                |    return nil
                |}
            """.trimMargin().keepIndentation()
        )
    }
}
