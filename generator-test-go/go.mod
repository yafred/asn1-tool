module generator-go.test

go 1.17

require github.com/yafred/asn1-go v0.0.4

replace generated-code => ../generator/target/generator-output/go

require generated-code v0.0.0-00010101000000-000000000000
