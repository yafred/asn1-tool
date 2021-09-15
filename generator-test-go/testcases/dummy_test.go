package testcases

import (
	"testing"

	"generated-code/dummy"
)

func TestHello(t *testing.T) {
	value := dummy.Hello()

	if value != "hello" {
		t.Fatal("Wrong")
	}
}
